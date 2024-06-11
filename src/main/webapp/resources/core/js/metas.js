document.addEventListener("DOMContentLoaded", function (){
    const nodoUl = document.getElementById("lista-seguimiento-metas")

    mostrarSeguimiento();

    async function mostrarSeguimiento() {
        try{
            const rutaMetasDefinidas = "metas/definidas"
            const respuestaMetas = await fetch(rutaMetasDefinidas)
            const metas = await respuestaMetas.json()

            const rutaSeguimiento = "metas/seguimiento"
            const respuestaSeguimiento = await fetch(rutaSeguimiento)
            console.log(respuestaSeguimiento)
            const totalMontosDeCategoriasConMetas = await respuestaSeguimiento.json()
            // Convertir el objeto JSON a un Map
            const totalMontosDeCategoriasConMetasMap = new Map(Object.entries(totalMontosDeCategoriasConMetas));
            //recorro mapa de montos gastados por categoria
            totalMontosDeCategoriasConMetasMap.forEach((valor, clave) =>{
                const metaArray = obtenerMeta(metas, clave)
                const meta = metaArray[0]
                console.log(meta)
                agregarContenidoAVistaMetas(meta, valor, clave)

            })
        }catch (error){
            console.log(error)
        }

    }

    function obtenerMeta(metas, clave){
        return  metas.filter(meta =>{
            const {categoriaMovimiento} = meta
            const {nombre} = categoriaMovimiento
            return nombre === clave
        })
    }

    function agregarContenidoAVistaMetas(meta, valor, clave){
        console.log(valor)
        const {montoMeta} = meta;
        const li = document.createElement('li')
        const spanContenedorCategoriaYMontos = document.createElement('span')
        spanContenedorCategoriaYMontos.classList.add("contenedor-cat-montos")
        //Agrego nombre de categoria
        const spanCategoria = document.createElement('span')
        spanCategoria.innerText = `${clave}`
        spanContenedorCategoriaYMontos.appendChild(spanCategoria)

        //agrego montos de meta y monto usado de esa categoria
        const spanContenedorMontos = document.createElement('span')
        spanContenedorMontos.classList.add("contenedor-montos")

        const strongMontoGastadoCategoria = document.createElement('strong')
        strongMontoGastadoCategoria.innerText = `$ ${valor}`
        spanContenedorMontos.appendChild(strongMontoGastadoCategoria)

        const spanMontoMeta = document.createElement('span')
        spanMontoMeta.innerText = `de $ ${montoMeta}`
        spanContenedorMontos.appendChild(spanMontoMeta)

        spanContenedorCategoriaYMontos.appendChild(spanContenedorMontos)

        //agrego barra de progreso
        const divContenedorBarra = document.createElement('div')
        divContenedorBarra.classList.add("progress")
        const spanBarra = document.createElement('div')
        spanBarra.classList.add("progress-bar")
        spanBarra.role = "progressbar"
        spanBarra.ariaValueNow = `${valor}`
        spanBarra.ariaValuemin="0"
        spanBarra.ariaValuemax=`${montoMeta}`
        spanBarra.style=`width: ${(valor/montoMeta) * 100}%`
        spanBarra.innerText= `${(valor/montoMeta) * 100}%`
        divContenedorBarra.appendChild(spanBarra)

        //AGREGO EL CONTENIDO AL <li>
        li.classList.add("list-group-item")
        li.classList.add("p-3")
        li.appendChild(spanContenedorCategoriaYMontos)
        li.appendChild(divContenedorBarra)
        nodoUl.appendChild(li)
    }
})