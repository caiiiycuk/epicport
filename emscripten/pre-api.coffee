_Epicport_CanSave = () ->
  if (Epicport['profile'])
    return true

  Epicport.login()
  return false

_Epicport_CanLoad = _Epicport_CanSave