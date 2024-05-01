function seleccionar(selector){
    return document.querySelector(selector)
}
function on(evento, elementoHtml,  callback){
    elementoHtml.addEventListener(evento, callback)
}
const iconoMenu = seleccionar(".toggle-sidebar-btn")

on('click', iconoMenu, function (e){
    seleccionar('body').classList.toggle('toggle-sidebar')
})

