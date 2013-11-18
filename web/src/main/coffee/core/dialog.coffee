Epicport.modalMessage = (title, message, callback) ->
  div = $('<div title="' + title + '">' + message + '</div>')
  div.dialog
    dialogClass: "modal"
    modal: true
    buttons:
      'Ok': () ->
        $(@).dialog 'close'
        if callback
          callback($(@))

Epicport.modalProgress = () ->
  div = $('<div class="progress_modal"></div>')
  $(document.body).append(div)
  () -> div.remove()