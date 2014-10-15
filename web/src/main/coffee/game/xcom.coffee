class Epicport.XCOM

  constructor: ->
    $("#xcom_data_link").click () ->
      $("#input_xcom_data_link").val($("#xcom_data_link").attr("href"))
      false

  start: ->
    #console.log('Loading openxcom.js')
    #$(document.body).append('<script type="text/javascript" src="' + options.js +  '"/>')
       
    Module['openxcom.data'] = "/storage/proxy?url=https://dl.dropboxusercontent.com/s/kp9i4z5qtpede1d/openxcom.data"

    startGame = () => $.ajax
      url: "/emscripten/xcom/openxcom.js"
      dataType: "script"
      xhr: () ->
        Module.setStatus "Downloading script (openxcom.js)"
        xhr = $.ajaxSettings.xhr()
        xhr.addEventListener "progress", (evt) ->
          if (evt.lengthComputable)
            Epicport.API.progress evt.loaded, evt.total
        xhr

    setTimeout startGame, 500
