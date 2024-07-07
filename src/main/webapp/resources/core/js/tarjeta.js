document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector("form");
    const apellidoYNombre = document.getElementById("apellidoYNombre");
    const numero = document.getElementById("numero");
    const fecha = document.getElementById("fecha");
    const cvv = document.getElementById("cvv");

    form.addEventListener("submit", function(event) {
        let valid = true;
        clearErrors();

        if (!apellidoYNombre.value) {
            showError(apellidoYNombre, "Este campo es obligatorio.");
            valid = false;
        } else if (!isValidName(apellidoYNombre.value)) {
            showError(apellidoYNombre, "Debe ingresar al menos un nombre y un apellido.");
            valid = false;
        }

        if (!numero.value) {
            showError(numero, "Este campo es obligatorio.");
            valid = false;
        } else if (numero.value.length !== 16) {
            showError(numero, "El numero de tarjeta debe tener 16 digitos.");
            valid = false;
        }

        if (!fecha.value) {
            showError(fecha, "Este campo es obligatorio.");
            valid = false;
        } else if (!isValidDateFormat(fecha.value)) {
            showError(fecha, "El formato de la fecha debe ser MM/AA.");
            valid = false;
        } else if (!isValidDate(fecha.value)) {
            showError(fecha, "La fecha de vencimiento debe ser mayor a 6 meses desde hoy.");
            valid = false;
        }

        if (!cvv.value) {
            showError(cvv, "Este campo es obligatorio.");
            valid = false;
        } else if (cvv.value.length < 3 || cvv.value.length > 4 || !isPasswordType(cvv)) {
            showError(cvv, "El CVV debe tener 3 o 4 caracteres");
            valid = false;
        }

        if (!valid) {
            event.preventDefault();
        }
    });

    function showError(input, message) {
        const error = document.createElement("p");
        error.textContent = message;
        error.style.color = "red";
        input.parentElement.appendChild(error);
    }

    function clearErrors() {
        const errors = document.querySelectorAll("p");
        errors.forEach(error => error.remove());
    }

    function isValidName(name) {
        const regex = /^[a-zA-Z]+\s+[a-zA-Z]+$/;
        return regex.test(name);
    }

    function isValidDate(date) {
        const [month, year] = date.split("/").map(Number);
        const currentDate = new Date();
        const expirationDate = new Date(`20${year}`, month - 1); // Ajusta el año a cuatro dígitos

        const sixMonthsFromNow = new Date();
        sixMonthsFromNow.setMonth(currentDate.getMonth() + 6);

        return expirationDate > sixMonthsFromNow;
    }
    function isValidDateFormat(date) {
        const regex = /^(0[1-9]|1[0-2])\/?([0-9]{2})$/;
        return regex.test(date);
    }
});
