class Epicport.Navbar

  constructor: ->
    @loginLink = $('#navbar_login_link')
    @logoutLink = $('#navbar_logout_link')
    @name = $('#navbar_name')
    $('#navbar_login_link, #navbar_logout_link').click () => 
      Epicport.login callback: () =>
        @reset()
      false
    @reset()


  reset: ->
    if (Epicport.profile)
      @loginLink.hide()
      @logoutLink.show()
      @name.html(Epicport.profile.first_name)
      @name.show()
    else
      @loginLink.show()
      @logoutLink.hide()
      @name.hide()