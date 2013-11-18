class Epicport.Login
  constructor: (options) ->
    Epicport.profile = options.data
    $(".login_modal").dialog("close")

Epicport.login = (options) ->
  $(".login_modal").dialog 
    modal: true
    close: options.callback

