document.addEventListener("DOMContentLoaded", function (){
    const nodoInputCalendario = document.getElementById("calendario")
    const nodoUl = document.getElementById("lista-movimientos")
    const canvas = document.getElementById('myChart')
    const nodoDivGrafico = document.getElementById("contenedor-grafico")
    const botonesExportar = document.getElementsByName("exportar")

    const chart = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Ingreso', 'Egreso'],
            datasets: [{
                label: 'Monto $',
                data: [0, 0],
                borderWidth: 1
            }]
        }
    })

    nodoInputCalendario.addEventListener("change", actualizarMovimientos)

    botonesExportar.forEach(boton =>{
        boton.addEventListener("click", exportarDatos)
    })

    async function actualizarMovimientos(e){
        const fecha = e.target.value
        if(fecha === ''){
            window.location = "/spring/movimientos"
        }
        const ruta = `movimientos/${fecha}`
        try{
            const respuesta =  await fetch(ruta)
            const movimientos = await respuesta.json()
            if(movimientos.length === 0){
                nodoUl.innerHTML = `<p>No hay movimientos disponibles.</p>`
                nodoDivGrafico.style.display = 'none';
                return
            }

            nodoDivGrafico.style.display = 'block';
             const movimientosString = movimientos.map(movimiento =>{
                 const {descripcion, categoria: { tipo, nombre, icono, color }, monto, id} = movimiento
                return `<li class="list-group-item item" style="background-color: ${color}80;">
                                <span>
                                    <span class="${icono}"></span>
                                    <span>${descripcion}</span><br>
                                    <span>${nombre}</span>
                                </span>
                                <span>
                                    <span class="${tipo.nombre==='INGRESO'?'tipo-ingreso':'tipo-egreso'}" >  ${tipo.nombre === 'INGRESO' ? `+ $${monto}` : `- $${monto}` }</span>
                                    <a href="/spring/movimientos/editar/${id}"><i class="bi bi-pencil"></i></a>
                                </span>
                    </li>`})
                 .join("")
            nodoUl.innerHTML = movimientosString

            const sumaDeIngresos = movimientos
                .filter(movimiento =>movimiento.categoria.tipo.nombre === "INGRESO")
                .map(movimiento => movimiento.monto)
                .reduce((acumulador,monto)=>acumulador+monto, 0)
            const sumaDeEgresos = movimientos
                .filter(movimiento =>movimiento.categoria.tipo.nombre === "EGRESO")
                .map(movimiento => movimiento.monto)
                .reduce((acumulador,monto)=>acumulador+monto, 0)

            actualizarGrafico(sumaDeIngresos, sumaDeEgresos)

        }catch (e){
            console.log(e)
        }

    }

    function actualizarGrafico(totalIngresos, totalEgresos) {
        const coloresIngreso = '#39cb29'; // Color verde para ingresos
        const coloresEgreso = '#c33838'; // Color rojo para egresos

        chart.data.datasets[0].backgroundColor = [coloresIngreso, coloresEgreso];
        chart.data.datasets[0].data = [totalIngresos, totalEgresos];
        chart.update();
    }

    async function exportarDatos(e) {
        //obtengo el tipo de documento a exportar del name del boton clickeado
        const tipoDeDoc = e.target.dataset.tipoDoc

        try{
            //espero obtener una respuesta al enviar una solicitud GET al controlador de movimientos.
            const respuesta = await fetch(`/spring/movimientos/exportar/${tipoDeDoc}`)
            //espero obtener un objeto javascript llamado blob que almacena los datos binarios del archivo.
            const blob = await respuesta.blob()
            const url = window.URL.createObjectURL(blob)
            const a = document.createElement('a')
            a.style.display = 'none'
            a.href = url
            a.download = `archivo.${tipoDeDoc.toLowerCase()}`
            document.body.appendChild(a)
            a.click()
            window.URL.revokeObjectURL(url)
        }catch (error){
            alert(error.message)
        }
    }
})