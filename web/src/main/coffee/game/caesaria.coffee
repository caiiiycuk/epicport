class Epicport.Caesaria

  start: ->
    startGame = () => $.ajax
      url: "/emscripten/caesaria/caesaria.js"
      dataType: "script"
      xhr: () ->
        Module.setStatus "Downloading script (caesaria.js)"
        xhr = $.ajaxSettings.xhr()
        xhr.addEventListener "progress", (evt) ->
          if (evt.lengthComputable)
            Epicport.API.progress evt.loaded, evt.total
        xhr

    setTimeout startGame, 500
