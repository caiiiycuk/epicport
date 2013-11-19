class Epicport.API 

  constructor: (options) ->
    self = @
    @canvas = new Epicport.Canvas(options)
    @game = options.game

    status = document.getElementById("status")
    progress = $("#progress")
    progress.progressbar value: 0

    Module = 
      arguments: ["-useOpenGL", "false", "-asyncBlit", "false", "-vSyncForOpenGL", "false", "-useOpenGLSmoothing", "false", "-mute", "false", "-playIntro", "false", "-language", "en-US", "-displayWidth", "960", "-displayHeight", "600"]
      
      screenIsReadOnly: true
      preRun: [
        () -> 
          Module['FS_createFolder']('/', 'home', true, true)
          Module['FS_createFolder']('/home', 'emscripten', true, true)
          Module['FS_createFolder']('/home/emscripten', '.openxcom', true, true)
          Module['FS_createFolder']('/home/emscripten', '.config', true, true)
          Module['FS_createFolder']('/home/emscripten/.config', '.openxcom', true, true)

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
          progress.progressbar "option", "value", parseInt(m[2]) * 100
          progress.progressbar "option", "max", parseInt(m[4]) * 100
        status.innerHTML = text

        if (text == '') 
          self.canvas.hideOverlay()

      totalDependencies: 0

      monitorRunDependencies: (left) ->
        @totalDependencies = Math.max(@totalDependencies, left)
        Module.setStatus (if left then "Preparing... (" + (@totalDependencies - left) + "/" + @totalDependencies + ")" else "All downloads complete.")

    Module.setStatus "Downloading..."

    @Module = Module

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

  pushSave: (filePtr) ->
    done = Epicport.modalProgress()

    file = Pointer_stringify(filePtr)
    fs_object = FS.findObject(file);
    contents = fs_object.contents;
    array = new Uint8Array(contents);

    $.ajax 
      url: '/xhr/storage/push'
      type: 'POST'
      data: array
      processData: false
      contentType: 'application/octet-stream'
      beforeSend: (request) ->
        request.setRequestHeader('X-Profile', JSON.stringify(Epicport['profile']))
        request.setRequestHeader('X-File-Name', file)
        request.setRequestHeader('X-Game', Epicport.API.game)
        request.setRequestHeader('X-csrf-token', $("meta[name='csrf-token']").attr("content"))
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
    loaders = []
    for file in files
      loaders.push @loadFile(file)
    
    async.parallel loaders, (error, files) -> 
      if error
        Epicport.modalMessage("Error", error)

      if files
        for file in files
          name = file.file.substring file.file.lastIndexOf('/') + 1
          parent = file.file.substring 0, file.file.lastIndexOf('/') + 1
          console.log "Creating file '" + name + "' in '" + parent + "'"
          FS.createDataFile(parent, name, file.data, true, true)#

      callback()

  loadFile: (fileName) ->
    (callback) ->
      $.ajax 
        url: '/xhr/storage/content/' + Epicport.API.game
        type: 'GET'
        data: 
          $.extend(fileName: fileName, Epicport.profile)
        success: (resp) ->
          callback null, 
            file: fileName
            data: resp
        error: (xhr, state, error) ->
          callback error, null