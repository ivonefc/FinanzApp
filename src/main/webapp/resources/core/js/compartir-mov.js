document.addEventListener('DOMContentLoaded', function() {
    // Funcionalidad para mostrar/ocultar detalles de compartir movimiento
    document.querySelectorAll('input[name="compartirMovimiento"]').forEach((elem) => {
        elem.addEventListener('change', (event) => {
            console.log('Cambio detectado en compartirMovimiento');
            document.getElementById('compartirDetalles').style.display = event.target.value === 'si' ? 'block' : 'none';
            if(event.target.value === 'si') {
                cargarAmigos();
            }
        });
    });
});

function cargarAmigos() {
    fetch('/usuarios/amigos')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            var selectAmigo = document.getElementById('amigo');
            selectAmigo.innerHTML = '';

            data.forEach(amigo => {
                var option = document.createElement('option');
                option.value = amigo.id;
                option.text = amigo.nombre;
                selectAmigo.add(option);
            });
        })
        .catch(error => console.error('Error al cargar amigos:', error));
}