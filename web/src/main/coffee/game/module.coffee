Epicport.makeModule = (options) ->
  canvas = options.canvas

  output = document.getElementById("output")
  status = document.getElementById("status")
  progress = $("#progress")
  progress.progressbar value: 0

  Module = 
    arguments: ["-useOpenGL", "false", "-asyncBlit", "false", "-vSyncForOpenGL", "false", "-useOpenGLSmoothing", "false", "-mute", "true", "-playIntro", "false", "-language", "en-US", "-displayWidth", "960", "-displayHeight", "600"]
    
    screenIsReadOnly: true
    preRun: []
    postRun: []
    
    print: (->
      output.value = ""
      (text) ->
        text = Array::slice.call(arguments_).join(" ")
        text = text.replace(/&/g, "&amp;")
        text = text.replace(/</g, "&lt;")
        text = text.replace(/>/g, "&gt;")
        text = text.replace("\n", "<br>", "g")
        output.value += text + "\n"
        output.scrollTop = 99999 # focus on bottom
    )()
    
    printErr: (text) ->
      text = Array::slice.call(arguments).join(" ")
      if 0 # XXX disabled for safety typeof dump == 'function') {
        dump text + "\n" # fast, straight to the real console
      else
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