// Compiled by ClojureScript 0.0-3190 {}
goog.provide('librarian.core');
goog.require('cljs.core');
goog.require('goog.dom');
goog.require('goog.events');
/**
 * Checks if the input field on the front-end has valid input and loads a spinner image to show work
 * in progress. Otherwise it shows a warning message if the input is not valid.
 */
librarian.core.validation_loader = (function librarian$core$validation_loader(){
var image = cljs.core.atom.call(null,"<img src='images/spinner.gif'>");
var warning = cljs.core.atom.call(null,"Please check your input");
var button = goog.dom.getElement("recommendbutton");
var display = goog.dom.getElement("load");
return goog.events.listen(button,"click",((function (image,warning,button,display){
return (function (event){
if(cljs.core.truth_(goog.dom.getElement("searchfield").checkValidity())){
return display.innerHTML = cljs.core.deref.call(null,image);
} else {
return display.innerHTML = cljs.core.deref.call(null,warning);
}
});})(image,warning,button,display))
);
});
librarian.core.validation_loader.call(null);
