class Epicport.Dune2

  constructor: ->
    Module['EM_MIDI_MUTED'] ||= false;

    soundControl = $('.opendune-toggle-sound')
    soundControl.click () ->
      if Module['EM_MIDI_TOGGLE_SOUND']
        Module['EM_MIDI_TOGGLE_SOUND']()
      else 
        return
        
      if Module['EM_MIDI_MUTED']
        soundControl.addClass('opendune-unmute')
        soundControl.removeClass('opendune-mute')
      else
        soundControl.removeClass('opendune-unmute')
        soundControl.addClass('opendune-mute')

    soundControl.show()

    $('.opendune-a-house').click () ->
      Module['arguments'] = ['-a']
      $(".dune2-select-house-dialog").dialog('close')

    $('.opendune-o-house').click () ->
      Module['arguments'] = ['-o']
      $(".dune2-select-house-dialog").dialog('close')

    $('.opendune-h-house').click () ->
      Module['arguments'] = ['-h']
      $(".dune2-select-house-dialog").dialog('close')      

  start: (jsFile) ->
    $(".dune2-select-house-dialog").dialog
      width: 650
      modal: true
      close: () ->
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
