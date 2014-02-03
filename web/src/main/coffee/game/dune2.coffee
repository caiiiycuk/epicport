class Epicport.Dune2

  constructor: ->
    # do nothing

  start: ->
    startGame = () => $.ajax
      url: "opendune.js"
      dataType: "script"
      xhr: () ->
        Module.setStatus "Downloading script (opendune.js)"
        xhr = $.ajaxSettings.xhr()
        xhr.addEventListener "progress", (evt) ->
          if (evt.lengthComputable)
            Epicport.API.progress evt.loaded, evt.total
        xhr

    setTimeout startGame, 500  
