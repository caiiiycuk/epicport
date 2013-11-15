Epicport.makeModule = (options) ->
  canvas = options.canvas

  status = document.getElementById("status")
  progress = $("#progress")
  progress.progressbar value: 0

  Module = 
    arguments: ["-useOpenGL", "false", "-asyncBlit", "false", "-vSyncForOpenGL", "false", "-useOpenGLSmoothing", "false", "-mute", "true", "-playIntro", "false", "-language", "en-US", "-displayWidth", "960", "-displayHeight", "600"]
    
    screenIsReadOnly: true
    preRun: [
      () -> 
        Module['FS_createFolder']('/', 'home', true, true)
        Module['FS_createFolder']('/home', 'emscripten', true, true)
        Module['FS_createFolder']('/home/emscripten', '.openxcom', true, true)
        Module['FS_createFolder']('/home/emscripten', '.config', true, true)
        Module['FS_createFolder']('/home/emscripten/.config', '.openxcom', true, true)
    ]
    postRun: []
    
    print: (-> (text) -> console.log text)()
    
    printErr: (text) ->
      text = Array::slice.call(arguments).join(" ")
      console.log text

    canvas: canvas.el()

    setStatus: (text) ->
      clearInterval Module.setStatus.interval  if Module.setStatus.interval
      m = text.match(/([^(]+)\((\d+(\.\d+)?)\/(\d+)\)/)
      if m
        text = m[1]
        progress.progressbar "option", "value", parseInt(m[2]) * 100
        progress.progressbar "option", "max", parseInt(m[4]) * 100
      status.innerHTML = text

      if (text == '') 
        canvas.hideOverlay()

    totalDependencies: 0

    monitorRunDependencies: (left) ->
      @totalDependencies = Math.max(@totalDependencies, left)
      Module.setStatus (if left then "Preparing... (" + (@totalDependencies - left) + "/" + @totalDependencies + ")" else "All downloads complete.")

  Module.setStatus "Downloading..."
  Module