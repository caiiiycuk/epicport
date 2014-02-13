class Epicport.API

  constructor: (options) ->
    self = @
    @canvas = new Epicport.Canvas(options)
    @game = options.game
    @audio = new Audio()
    @audio.volume = 0.5
    @files = {}

    status = document.getElementById("status")
    progress = $("#progress")
    progress.progressbar value: 0

    Module = 
      arguments: options.arguments
      
      screenIsReadOnly: true
      preRun: [
        () -> 
          if (options.preRun)
            options.preRun()

          Epicport.API.createFs()
      ]
      postRun: []
      
      print: (-> (text) -> console.log text)()
      
      printErr: (text) ->
        text = Array::slice.call(arguments).join(" ")
        console.log text

      canvas: @canvas.el()

      setStatus: (text) ->
        clearInterval Module.setStatus.interval  if Module.setStatus.interval
        m = text.match(/([^(]+)\((\d+(\.\d+)?)\/(\d+)\)/)
        if m
          text = m[1]
          Epicport.API.progress(parseInt(m[2]) * 100, parseInt(m[4]) * 100)
        status.innerHTML = text

        if (text == '') 
          self.canvas.hideOverlay()

      totalDependencies: 0

      monitorRunDependencies: (left) ->
        @totalDependencies = Math.max(@totalDependencies, left)
        Module.setStatus (if left then "Preparing... (" + (@totalDependencies - left) + "/" + @totalDependencies + ")" else "All downloads complete.")

    Module.setStatus "Downloading..."

    @Module = Module

  progress: (value, max) ->
    progress = $("#progress")
    progress.progressbar "option", "value", value
    progress.progressbar "option", "max", max


  module: () -> @Module    

  canSave: () ->
    if (Epicport.profile)
      return true

    Epicport.login callback: () ->
      if (Epicport.profile)
        Epicport.API.createFs()
        Epicport.modalMessage(Epicport.i18n.html_login_success_title, Epicport.i18n.html_can_save_desc)

    return false

  canLoad: () ->
    if (Epicport.profile)
      return true

    Epicport.login callback: () ->
      if (Epicport.profile)
        Epicport.API.createFs()
        Epicport.modalMessage(Epicport.i18n.html_login_success_title, Epicport.i18n.html_can_load_desc)

    return false

  selectLoadFileDialog: (extensionPtr, callback, hideFileInputField) ->
    Epicport.API.selectFileDialog(extensionPtr, callback, true)

  selectSaveFileDialog: (extensionPtr, callback, hideFileInputField) ->
    Epicport.API.selectFileDialog(extensionPtr, callback, false)

  selectFileDialog: (extensionPtr, callback, hideFileInputField) ->
    extension = Module['Pointer_stringify'](extensionPtr)

    if hideFileInputField
      $('.select-file-input').hide()
    else
      $('.select-file-input').show()

    files = Object.keys(Epicport.API.files)

    if files.length
      $(".select-file-dialog ul").empty()
      for file in files
        filename = file.substring file.lastIndexOf('/') + 1
        $(".select-file-dialog ul").append("<li>" + filename + "</li>")

    $(".select-file-dialog ul > li").off('click')
    $(".select-file-dialog ul > li").click (e) ->
      success($(e.target).html())
      $(".select-file-dialog").dialog('close')

    unless (typeof Module == 'undefined')
      Module['disable_sdl_envents'] = true

    success = (filename) ->
      unless Epicport.API.selectFileDialogPtr
        Epicport.API.selectFileDialogPtr = Module['_malloc'](128)

      Module['writeStringToMemory'](filename, Epicport.API.selectFileDialogPtr)
      Module['dunCall']('vi', callback, [Epicport.API.selectFileDialogPtr])

      unless (typeof Module == 'undefined')
        Module['disable_sdl_envents'] = false

    okButton = {
      text: Epicport.i18n.html_ok
      click: () -> 
        selected = $('#select-file-dialog-file').val()
        success(selected + "." + extension) if selected
        $(@).dialog "close"
    }


    cancelButton = {
      text: Epicport.i18n.html_cancel
      click: () -> 
        $(@).dialog "close"

        unless (typeof Module == 'undefined')
          Module['disable_sdl_envents'] = false
    }

    if hideFileInputField
      buttons = [cancelButton]
    else
      buttons = [okButton, cancelButton]

    $(".select-file-dialog").dialog
      width: 650
      modal: true
      buttons: buttons

  pushSave: (filePtr) ->
    done = Epicport.modalProgress()

    file = Module['Pointer_stringify'](filePtr)
    
    if (Module['FS_findObject'])
      fs_object = Module['FS_findObject'](file)
    else
      fs_object = FS.findObject(file)

    contents = fs_object.contents
    array = new Uint8Array(contents)

    Epicport.API.files[file] = 1

    $.ajax 
      url: '/xhr/storage/push'
      type: 'POST'
      data: array
      processData: false
      contentType: 'application/octet-stream'
      beforeSend: (request) ->
        request.setRequestHeader('X-Profile', JSON.stringify(identity: Epicport['profile'].identity))
        request.setRequestHeader('X-File-Name', file)
        request.setRequestHeader('X-Game', Epicport.API.game)
        request.setRequestHeader('X-CSRF-Token', $("meta[name='csrf-token']").attr("content"))
      success: (resp) ->
        done()
        Epicport.modalMessage(Epicport.i18n.html_success, Epicport.i18n.html_game_saved)
      error: (xhr, state, error) ->
        done()
        status = xhr.status || 500
        error  = error || "Unknown error"

        Epicport.modalMessage("Error (" + status+ ")", "(" + status + "): " + error)

  createFs: () ->
    unless Epicport.profile
      return

    if Epicport.API.fsCreated
      return

    done = Epicport.modalProgress()

    $.ajax 
      url: '/xhr/storage/list/' + Epicport.API.game
      type: 'GET'
      data: Epicport.profile
      success: (resp) ->
        Epicport.API.loadFiles resp.files, () ->
          done()
          Epicport.API.fsCreated = true
      error: (xhr, state, error) ->
        done()
        status = xhr.status || 500
        error  = error || "Unknown error"

        Epicport.modalMessage("Error (" + status+ ")", "(" + status + "): " + error)

  loadFiles: (files, callback) ->
    if files.length
      $(".select-file-dialog ul > span").hide()

    loaders = []
    for file in files
      Epicport.API.files[file] = 1
      loaders.push @loadFile(file)
    
    async.parallel loaders, (error, files) -> 
      if error
        Epicport.modalMessage("Error", error)

      if files
        for file in files
          name = file.file.substring file.file.lastIndexOf('/') + 1
          parent = file.file.substring 0, file.file.lastIndexOf('/') + 1
          console.log "Creating file '" + name + "' in '" + parent + "'"
          Module['FS_createDataFile'](parent, name, file.data, true, true)

      callback()

  loadFile: (fileName) ->
    (callback) ->
      $.ajax 
        url: '/xhr/storage/content/' + Epicport.API.game
        type: 'GET'
        data: 
          $.extend(fileName: fileName, Epicport.profile)
        beforeSend: (xhr) ->
          xhr.overrideMimeType "text/plain; charset=x-user-defined" 
        success: (resp) ->
          callback null, 
            file: fileName
            data: resp
        error: (xhr, state, error) ->
          callback error, null

  playMusic: (filePtr, loops) ->
    file = Module['Pointer_stringify'](filePtr)
    name = file.substring file.lastIndexOf('/') + 1
    Epicport.API.audio.src = "/" + name
    Epicport.API.audio.play()

  volumeMusic: (volume) ->
    Epicport.API.audio.volume = volume / 128.0

  haltMusic: ->
    Epicport.API.audio.pause()