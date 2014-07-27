jQuery.fn.encHTML = function() {
  return this.each(function(){
    var me   = jQuery(this);
    var html = me.html();
    me.html(html.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'));
  });
};
 
jQuery.fn.decHTML = function() {
  return this.each(function(){
    var me   = jQuery(this);
    var html = me.html();
    me.html(html.replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>'));
  });
};
 
jQuery.fn.isEncHTML = function(str) {
  if(str.search(/&amp;/g) != -1 || str.search(/&lt;/g) != -1 || str.search(/&gt;/g) != -1)
    return true;
  else
    return false;
};
 
jQuery.fn.decHTMLifEnc = function(){
  return this.each(function(){
    var me   = jQuery(this);
    var html = me.html();
    if(jQuery.fn.isEncHTML(html))
      me.html(html.replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>'));
  });
};