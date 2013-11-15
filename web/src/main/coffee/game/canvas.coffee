class Epicport.Canvas

  constructor: (options) ->
    @overlay = $('.canvas_overlay')
    @canvas = document.getElementById("canvas")

    start = () -> 
      console.log('Loading ' + options.js +  '...')
      $.getScript(options.js)
      #$(document.body).append('<script type="text/javascript" src="' + options.js +  '"/>')
    clickToStart = $('.click_to_start')
    
    clickToStart.click () => 
      clickToStart.hide()
      $('.loader').css('display', 'table-cell')
      setTimeout start, 500

  el: () ->
    @canvas

  hideOverlay: () ->
    @overlay.hide()