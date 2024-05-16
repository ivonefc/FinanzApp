document.addEventListener("DOMContentLoaded", function (){
    const nodoInputCalendario = document.getElementById("calendario")
    const nodoUl = document.getElementById("lista-movimientos")
    const canvas = document.getElementById('myChart')

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
            console.log(movimientos)

             const movimientosString = movimientos.map(movimiento =>{
                return `<li class="list-group-item">
                            <span>
                            <span>
                                <span>${movimiento.descripcion}</span><br>
                                <span>${movimiento.categoria.nombre}</span>
                            </span>
                               <span>${movimiento.monto}</span>
                            </span>
                            <a href="/spring/movimientos/editar/${movimiento.id}"><i class="bi bi-pencil"></i></a>
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