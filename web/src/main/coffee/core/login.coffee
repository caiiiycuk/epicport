class Epicport.Login
  constructor: (options) ->
    Epicport.profile = options.data
    
    $.cookie 'profile', JSON.stringify(Epicport.profile), 
      expires: 356
      path: '/'

    $(".login_modal").dialog("close")

Epicport.login = (options) ->
  options ||= {}
  $(".login_modal").dialog 
    modal: true
    width: 500
    close: options.callback