package com.epicport.action

import xitrum.Action
import com.epicport.i18n.I18N

trait RenderHelper extends Action {

  import I18N._
  
  def renderAlternateUris() = {
    val altUri = alternateUri(request.getUri)
    val links = languages.filter(_ != language).map { lang =>
      <link rel="alternate" hreflang={ lang } lang={ lang } href={ altUri(lang) } />
    }
    links.mkString
  }
  
  def yandexMetrika = 
"""
    <!-- Yandex.Metrika counter --><script type="text/javascript">(function (d, w, c) { (w[c] = w[c] || []).push(function() { try { w.yaCounter23247073 = new Ya.Metrika({id:23247073, webvisor:true, clickmap:true, trackLinks:true, accurateTrackBounce:true}); } catch(e) { } }); var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () { n.parentNode.insertBefore(s, n); }; s.type = "text/javascript"; s.async = true; s.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//mc.yandex.ru/metrika/watch.js"; if (w.opera == "[object Opera]") { d.addEventListener("DOMContentLoaded", f, false); } else { f(); } })(document, window, "yandex_metrika_callbacks");</script><noscript><div><img src="//mc.yandex.ru/watch/23247073" style="position:absolute; left:-9999px;" alt="" /></div></noscript><!-- /Yandex.Metrika counter -->
"""
  
  def googleAnalytics =
"""
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-46144566-1', 'auto');
  ga('send', 'pageview');

</script>
"""
}