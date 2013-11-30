class Epicport.Canvas

  constructor: (options) ->
    @overlay = $('.canvas_overlay')
    @canvas = document.getElementById("canvas")

    clickToStart = $('.click_to_start')
    
    clickToStart.click () => 
      clickToStart.hide()
      $('.loader').css('display', 'table-cell')
      options.start()

  el: () ->
    @canvas

  hideOverlay: () ->
    @overlay.hide()