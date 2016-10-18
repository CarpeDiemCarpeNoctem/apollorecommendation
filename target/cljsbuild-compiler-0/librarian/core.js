// Compiled by ClojureScript 0.0-3190 {}
goog.provide('librarian.core');
goog.require('cljs.core');
goog.require('goog.dom');
goog.require('goog.events');
librarian.core.loader = (function librarian$core$loader(){
var image = cljs.core.atom.call(null,"<img src='images/spinner.gif'>");
var button = goog.dom.getElement("recommendbutton");
var display = goog.dom.getElement("load");
return goog.events.listen(button,"click",((function (image,button,display){
return (function (event){
return display.innerHTML = cljs.core.deref.call(null,image);
});})(image,button,display))
);
});
librarian.core.loader.call(null);
