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

  start: ->
    startGame = () => $.ajax
      url: "opendune.js"
      dataType: "script"
      xhr: () ->
        Module.setStatus "Downloading script (opendune.js)"
        xhr = $.ajaxSettings.xhr()
        xhr.addEventListener "progress", (evt) ->
          if (evt.lengthComputable)
            Epicport.API.progress evt.loaded, evt.total
        xhr

    setTimeout startGame, 500  
