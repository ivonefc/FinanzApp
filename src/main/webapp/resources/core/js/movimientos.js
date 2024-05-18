document.addEventListener("DOMContentLoaded", function (){
    const nodoInputCalendario = document.getElementById("calendario")
    const nodoUl = document.getElementById("lista-movimientos")
    const canvas = document.getElementById('myChart')
    const nodoDivGrafico = document.getElementById("contenedor-grafico")

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
                 const {descripcion, categoria: { tipo, nombre }, monto, id} = movimiento
                return `<li class="list-group-item item">
                                <span>
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
        chart.data.datasets[0].data = [totalIngresos, totalEgresos]
        chart.update()
    }
})