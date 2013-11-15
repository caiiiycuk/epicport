class Epicport.Login
  constructor: (options) ->
    Epicport.profile = options.data

Epicport.login = () ->
  $( ".login_modal" ).dialog modal: true

