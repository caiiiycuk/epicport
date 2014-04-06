class Epicport.Ttd

  constructor: ->

  start: (jsFile) ->
    $(".canvas_container").width($(window).width())
    $(".canvas_container").height($(window).height())
    $(window).scrollTop($(".canvas_container").offset().top)

    Epicport.API.Module.arguments.push('-r')
    Epicport.API.Module.arguments.push($(".canvas_container").width() + 'x' + $(".canvas_container").height())

    startGame = () => $.ajax
      url: jsFile
      dataType: "script"
      xhr: () ->
        Module.setStatus "Downloading script (" + jsFile + ")"
        xhr = $.ajaxSettings.xhr()
        xhr.addEventListener "progress", (evt) ->
          if (evt.lengthComputable)
            Epicport.API.progress evt.loaded, evt.total
        xhr

    setTimeout startGame, 500