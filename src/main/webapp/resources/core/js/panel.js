//GRAFICO CATEGORIAS
document.addEventListener("DOMContentLoaded", async function () {
    const canvas = document.getElementById('myChart');
    const nodoDivGraficoCategorias = document.getElementById("contenedor-grafico-categorias");
    const nodoDivGraficoIngresosEgresos = document.getElementById("contenedor-grafico-ingresos-egresos");
    const selectMeses = document.getElementById('mesesSelect');
    const nodo = document.getElementById("movimientosVacios")

    // Seleccionar el mes actual (de 0 a 11) y actualizar el gráfico
    const mesActual = new Date().getMonth();
    mesesSelect.selectedIndex = mesActual;
    actualizarGraficoPorMes(mesActual+1);

    // Escuchar el cambio en el select de meses
    mesesSelect.addEventListener('change', async function () {
        const mesSeleccionado = parseInt(mesesSelect.value);
        await actualizarGraficoPorMes(mesSeleccionado+1);
    });

    //ACTUALIZAR GRAFICO
    async function actualizarGraficoPorMes(mesSeleccionado) {
        try {
            const montosMap = await obtenerMontosPorCategoriaPorMes(mesSeleccionado);

            if (!montosMap) {
                console.error('No data available for the selected month.');
                return;
            }

            // Extraer categorías y montos del Map
            const nombresCategorias = [];
            const montos = [];

            for (const [categoria, monto] of montosMap) {
                nombresCategorias.push(categoria);
                montos.push(monto);
            }

            const data = {
                labels: nombresCategorias,
                datasets: [{
                    label: 'Monto $',
                    data: montos,
                    borderWidth: 1,
                    hoverOffset: 10
                }]
            };

            // Destruir gráfico anterior si existe
            if (window.chart) {
                window.chart.destroy();
            }

            // Crear nuevo gráfico
            window.chart = new Chart(canvas, {
                type: 'doughnut',
                data: data,
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },

                    }
                }
            });
            nodo.innerHTML = '';
            if(montos.length===0){
                nodo.innerHTML = `<p>No hay movimientos registrados.</p>`
            }
        } catch (error) {
            console.error('Error fetching or processing data:', error);
        }

    }
});


//GRAFICO LINEAS
document.addEventListener("DOMContentLoaded", async function () {
    try {
        const canvasLineas = document.getElementById('myChartlineas');
        const ingresosPorMes = [];
        const egresosPorMes = [];

        for(mes= 1; mes<=12; mes++){
            movimientos = await obtenerMontosPorTipoDeCategoriaPorMes(mes);
            const ingresos = movimientos.get("ingresos");
            const egresos = movimientos.get("egresos");
            let sumaIngresos = 0;
            let sumaEgresos = 0;
            for(ingreso of ingresos) {
                sumaIngresos += ingreso.monto;
            }
            for(egreso of egresos) {
                sumaEgresos += egreso.monto;
            }
            ingresosPorMes.push(sumaIngresos);
            egresosPorMes.push(sumaEgresos);
        }
        if (!canvasLineas) {
            console.error('No se encontró el canvas con el ID canvasiye en el DOM');
            return;
        }


        const labels = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
        const dataLineas = {
            labels: labels,
            datasets: [{
                label: 'Ingresos $',
                data: ingresosPorMes,
                fill: false,
                borderColor: 'rgb(41, 224, 110)',
                backgroundColor: 'rgb(41, 224, 110)',
                tension: 0.1
            },
            {
                label: 'Egresos $',
                data: egresosPorMes,
                fill: false,
                borderColor: 'rgb(255, 65, 53)',
                backgroundColor: 'rgb(255, 65, 53)',
                tension: 0.1
            }]
        };

        const chartLineas = new Chart(canvasLineas, {
            type: 'line',
            data: dataLineas
        });

        const mov = obtenerIngresos();
        const fyh = mov.fechayHora;
    } catch (error) {
                 console.error('Error fetching or processing data:', error);
    }

});




// FUNCION PARA CALCULAR EL SALDO TOTAL
async function calcularSaldoTotal() {
    try {
        const ingresos = await obtenerIngresos();
        const egresos = await obtenerEgresos();

        const totalIngresos = ingresos.reduce((total, ingreso) => total + ingreso.monto, 0);
        const totalEgresos = egresos.reduce((total, egreso) => total + egreso.monto, 0);

        const saldoTotal = totalIngresos - totalEgresos;


        const nodoSaldoTotal = document.getElementById('saldoTotal');
        nodoSaldoTotal.textContent = `$${saldoTotal}`;

        return saldoTotal;
    } catch (error) {
        console.error('Error al calcular el saldo total:', error);
        const nodoSaldoTotal = document.getElementById('saldoTotal');
        nodoSaldoTotal.textContent = 'Error al calcular el saldo total';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    calcularSaldoTotal();
});


//FUNCIONES QUE TRAEN COSAS DEL CONTROLADOR

   async function obtenerIngresos() {
          try {
              const respuesta = await fetch('panel/ingresos');
              if (!respuesta.ok) {
                  throw new Error(`HTTP error! status: ${respuesta.status}`);
              }
              const ingresos = await respuesta.json();
              return ingresos;
          } catch (error) {
              console.error('Error:', error);
          }
      }


      async function obtenerEgresos() {
            try {
                const respuesta = await fetch('panel/egresos');
                if (!respuesta.ok) {
                    throw new Error(`HTTP error! status: ${respuesta.status}`);
                }
                const egresos = await respuesta.json();
                return egresos;
            } catch (error) {
                console.error('Error:', error);
            }
      }

async function obtenerMontosPorCategoriaPorMes(mesSeleccionado) {
    try {
        const egresos = await obtenerEgresos();
        const egresosFiltrados = []; //Todos los egresos para el mes elegido

                // Recorrer egresos y filtrar por mes
                for (let egreso of egresos) {
                    const mes = egreso.fechayHora[1];
                    if (mes === mesSeleccionado) {
                        egresosFiltrados.push(egreso);
                    }
                }
                const montosPorCategoria = new Map();
                for(let egreso of egresosFiltrados){

                    const categoria = egreso.categoria.nombre;
                    if (!montosPorCategoria.has(categoria)) {
                        montosPorCategoria.set(categoria, 0);
                    }
                    montosPorCategoria.set(categoria, montosPorCategoria.get(categoria) + egreso.monto);
                }
                return montosPorCategoria;
            } catch (error) {
                console.error('Error al calcular los egresos por mes:', error);
    }
}

async function obtenerMontosPorTipoDeCategoriaPorMes(mesSeleccionado) {
    try {
        const ingresos = await obtenerIngresos();
        const egresos = await obtenerEgresos();
        const egresosFiltrados = []; //Todos los egresos para el mes elegido
        const ingresosFiltrados = []; //Todos los ingresos para el mes elegido

        // Recorrer egresos y filtrar por mes
        for (let egreso of egresos) {
            const mes = egreso.fechayHora[1];
            if (mes === mesSeleccionado) {
                egresosFiltrados.push(egreso);
            }
        }
        for (let ingreso of ingresos) {
            const mes = ingreso.fechayHora[1];
            if (mes === mesSeleccionado) {
                ingresosFiltrados.push(ingreso);
            }
        }
        const montosPorTipoDeCategoria = new Map();
        montosPorTipoDeCategoria.set("ingresos",ingresosFiltrados);
        montosPorTipoDeCategoria.set("egresos",egresosFiltrados);

        return montosPorTipoDeCategoria;
    } catch (error) {
        console.error('Error al calcular los egresos por mes:', error);
    }
}