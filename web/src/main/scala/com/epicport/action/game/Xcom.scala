package com.epicport.action.game

import xitrum.annotation.GET
import xitrum.Action
import xitrum.annotation.First

@GET("/:lang/xcom")
class Xcom extends GameLayout {

  def title = t("html_xcom_title")
  def description = t("html_xcom_description")
  def keywords = t("html_xcom_keywords")
  
  def execute() {
    respondView()
  }
  
}