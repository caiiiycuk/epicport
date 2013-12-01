class Epicport.XCOM

  constructor: ->
    $("#xcom_data_link").click () ->
      $("#input_xcom_data_link").val($("#xcom_data_link").attr("href"))
      false

  start: ->
    #console.log('Loading openxcom.js')
    #$(document.body).append('<script type="text/javascript" src="' + options.js +  '"/>')
    
    $(".openxcom_eula").dialog
      width: 650
      modal: true
      buttons: [
        {
          text: Epicport.i18n.html_ok
          click: () -> 
            $(@).dialog "close"
            
            Module['openxcom.data'] = "/storage/proxy?url=" + $("#input_xcom_data_link").val()

            startGame = $.getScript("openxcom.js")
            setTimeout startGame, 500
        },
        {
          text: Epicport.i18n.html_cancel
          click: () -> 
            $(@).dialog "close"
        }
      ]