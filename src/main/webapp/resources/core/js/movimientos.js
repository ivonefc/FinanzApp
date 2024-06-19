document.addEventListener("DOMContentLoaded", function () {
    const nodoInputCalendario = document.getElementById("calendario");
    const nodoUl = document.getElementById("lista-movimientos");
    const canvas = document.getElementById('myChart');
    const nodoDivGrafico = document.getElementById("contenedor-grafico");

    // Definir los colores directamente en JavaScript
    const colorIngreso = 'rgba(57, 203, 41)'; // Color Ingreso con opacidad 20%
    const colorEgreso = 'rgba(195, 56, 56)'; // Color Egreso con opacidad 20%

    const chart = new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Ingreso', 'Egreso'],
            datasets: [{
                label: 'Monto $',
                data: [0, 0],
                backgroundColor: [
                    colorIngreso,
                    colorEgreso
                ],
            }]
        }
    });

    nodoInputCalendario.addEventListener("change", actualizarMovimientos);

    async function actualizarMovimientos(e) {
        const fecha = e.target.value;
        if (fecha === '') {
            window.location = "/spring/movimientos";
        }
        const ruta = `movimientos/${fecha}`;
        try {
            const respuesta = await fetch(ruta);
            const movimientos = await respuesta.json();
            if (movimientos.length === 0) {
                nodoUl.innerHTML = `<p>No hay movimientos disponibles.</p>`;
                nodoDivGrafico.style.display = 'none';
                return;
            }

            nodoDivGrafico.style.display = 'block';
            const movimientosString = movimientos.map(movimiento => {
                const { descripcion, categoria: { tipo, nombre, icono, color }, monto, id } = movimiento;
                return `<li class="list-group-item item" style="background-color: ${color}80;">
                            <span>
                                <span class="${icono}"></span>
                                <span>${descripcion}</span><br>
                                <span>${nombre}</span>
                            </span>
                            <span>
                                <span class="${tipo.nombre === 'INGRESO' ? 'tipo-ingreso' : 'tipo-egreso'}">
                                    ${tipo.nombre === 'INGRESO' ? `+ $${monto}` : `- $${monto}`}
                                </span>
                                <a href="/spring/movimientos/editar/${id}"><i class="bi bi-pencil btn btn-outline-dark mx-1"></i></a>
                            </span>
                </li>`;
            }).join("");
            nodoUl.innerHTML = movimientosString;

            const sumaDeIngresos = movimientos
                .filter(movimiento => movimiento.categoria.tipo.nombre === "INGRESO")
                .map(movimiento => movimiento.monto)
                .reduce((acumulador, monto) => acumulador + monto, 0);
            const sumaDeEgresos = movimientos
                .filter(movimiento => movimiento.categoria.tipo.nombre === "EGRESO")
                .map(movimiento => movimiento.monto)
                .reduce((acumulador, monto) => acumulador + monto, 0);

            actualizarGrafico(sumaDeIngresos, sumaDeEgresos);

        } catch (e) {
            console.log(e);
        }
    }

    function actualizarGrafico(totalIngresos, totalEgresos) {
        chart.data.datasets[0].data = [totalIngresos, totalEgresos];
        chart.update();
    }
});
