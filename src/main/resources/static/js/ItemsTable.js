let items = [];
let itemsOnPage = [];
let category = [];
let units = [];
let headers = [ "Имя", "Цена", "Кол-во в ед.", "Наценка (%)", "Видимость", "Тип", "Категория", "Ед. изм.", "Состав","Удалить"];

getItems();

function sortItems(){
    let option = document.getElementById('sortBy').value;
    if(option==='categories'){
        items.sort(function(a, b) {
            let nameA = a.categories.categoriesName.toLowerCase();
            let nameB = b.categories.categoriesName.toLowerCase();

            if (nameA < nameB) return -1;
            if (nameA > nameB) return 1;
            return 0;
        });
    }else if(option==='alf'){
        items.sort(function(a, b) {
            let nameA = a.nameOfItems.toLowerCase();
            let nameB = b.nameOfItems.toLowerCase();

            if (nameA < nameB) return -1;
            if (nameA > nameB) return 1;
            return 0;
        });
    }else {
        items.sort(function(a, b) {
            let nameA = a.id;
            let nameB = b.id;

            if (nameA < nameB) return -1;
            if (nameA > nameB) return 1;
            return 0;
        });
    }
    displayTable();
}
function displayTable() {
    const tableBody = document.getElementById('table');
    tableBody.innerHTML = ''; // Clear the existing table content
    createTableTitle();

    // Get the selected filter values
    const categoryFilter = document.getElementById('categoryFilter').value;
    const viewFilter = document.getElementById('viewFilter').value;
    const typeFilter = document.getElementById('typeFilter').value;

    // Filter items based on selected filters
    const filteredItems = items.filter(function(item) {
        let categoryMatch = true;
        let viewMatch = true;
        let typeMatch = true;

        if (categoryFilter !== 'null') {
            categoryMatch = (item.categories.categoriesName === categoryFilter);
        }
        if (viewFilter !== 'null') {
            viewMatch = (item.view === (viewFilter === 'true'));
        }
        if (typeFilter !== 'null') {
            typeMatch = (item.typeOfItem === (typeFilter === 'true' ));
        }

        return categoryMatch && viewMatch && typeMatch;
    });

    // Append filtered items to the table
    filteredItems.forEach(function(item) {
        tableBody.appendChild(createTableRow(item));
    });
}

function createTableRow(item) {
    let row = document.createElement('tr');

    let nameInput = createInput('text', item,'nameOfItems', true);
    let priceInput = createInput('number', item,'priceToPage', true, 0.01);
    let unitPriceInput = createInput('number', item,'unitPriceToPage', true, 0.001);
    let markupTd = document.createElement('td');
    markupTd.textContent =item.markup;
    let markupCell = markupTd;

    let viewSelect = createSelect(['true', 'false'], ['Виден', 'Не виден'], item.view, item,'view');
    let typeOfItemSelect = createSelect(['true', 'false'], ['Кухня', 'Бар'],item.typeOfItem, item,'typeOfItem');

    let categoriesSelect = createSelectObject(
        category,
        category.map(function(categor) { return categor.categoriesName; }),
        item.categories.id,
        item,
        'categories'
    );

    let unitsSelect = createSelectObject(
        units,
        units.map(function(unit) { return unit.name; }),
        item.unit.id,
        item,
        'unit'
    );

    let editLink = document.createElement('td');
    let editLinkAnchor = document.createElement('a');
    editLinkAnchor.href = '/admin/items/' + item.id;
    editLinkAnchor.className = 'btnchange';
    editLinkAnchor.textContent = 'Изменить';
    editLink.appendChild(editLinkAnchor);

    let deleteButtonTd = document.createElement('td');
    let deleteButton = document.createElement('button');
    deleteButton.className = 'btnchange';
    deleteButton.value = 'Удалить';
    deleteButton.onclick = function() {
        deleteItem(item.id);
    };
    deleteButtonTd.appendChild(deleteButton);
    // Добавляем ячейки в строку
    row.appendChild(nameInput);
    row.appendChild(priceInput);
    row.appendChild(unitPriceInput);
    row.appendChild(markupCell);
    row.appendChild(viewSelect);
    row.appendChild(typeOfItemSelect);
    row.appendChild(categoriesSelect);
    row.appendChild(unitsSelect);
    row.appendChild(editLink);
    row.appendChild(deleteButtonTd);

    return row;
}

// Вспомогательная функция для создания input элемента
function createInput(type, item,fieldName, required, step) {
    let input = document.createElement('input');
    input.type = type;
    input.value = item[fieldName];
    if (required) input.required = true;
    if (step) input.step = step;
    input.addEventListener('input', function() {
            // Обновляем свойство в объекте item при изменении значения в поле ввода
            item[fieldName] = input.value;
        });
    return createCell(input);
}

// Вспомогательная функция для создания td элемента
function createCell(content) {
    let cell = document.createElement('td');
    cell.appendChild(content);
    return cell;
}
function createSelectObject(values, texts, selectedValue, item, fieldName) {
    let select = document.createElement('select');

    for (let i = 0; i < values.length; i++) {
        let option = document.createElement('option');
        option.value = values[i].id; // Assuming values[i] is an object with an 'id' property
        option.text = texts[i];
        if (values[i].id === selectedValue) option.selected = true;
        select.appendChild(option);
    }

    // Добавляем обработчик события для изменения выбора
    select.addEventListener('change', function() {
        // Find the selected category object by its id
        item[fieldName] = category.find(obj => obj.id === parseInt(select.value, 10));
    });

    return createCell(select);
}
// Вспомогательная функция для создания select элемента
function createSelect(values, texts, selectedValue, item, fieldName) {
    let select = document.createElement('select');

    for (let i = 0; i < values.length; i++) {
        let option = document.createElement('option');
        option.value = values[i];
        option.text = texts[i];
        // Convert selectedValue to string for comparison
        if (String(values[i]) === String(selectedValue)) {
            option.selected = true;
        }
        select.appendChild(option);
    }

    // Добавляем обработчик события для изменения выбора
    select.addEventListener('change', function() {
        item[fieldName] = select.value;
    });

    return createCell(select);
}

function createTableTitle() {
    const tableElement = document.getElementById('table');
    // Создаем строку заголовков
    let headerRow = document.createElement('tr');

    // Добавляем заголовки в строку
    headers.forEach(function(headerText) {
        let th = document.createElement('th');
        th.textContent = headerText;
        headerRow.appendChild(th);
    });

    // Добавляем строку заголовков в thead
    tableElement.appendChild(headerRow);
}
function getItems() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/admin/items/getAll', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            items = JSON.parse(xhr.responseText);
            getUnits();
            getCategories();
        }
    };
    xhr.send();
}
function getUnits() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/admin/items/getUnits', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            units = JSON.parse(xhr.responseText);
        }
    };
    xhr.send();
}
function getCategories() {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/admin/items/getCategories', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            category = JSON.parse(xhr.responseText);
            addOptionsToCategoryFilter();
            displayTable();
        }
    };
    xhr.send();
}
function saveChanges(){
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/admin/items/saveAll', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200){
            getItems();
        }
        console.log(xhr.responseText);
    };
    xhr.send(JSON.stringify(items));
}

function showMessage(message='Откройте смену!!!',color='red'){
    let messageElement = document.getElementById('messages');
    messageElement.style.fontSize = "26px";
    messageElement.style.color = color;
    messageElement.textContent = message;
    let displayTime = 5000; // например, 5000 миллисекунд (5 секунд)
    setTimeout(function() {
        messageElement.textContent = ''; // Очищаем содержимое элемента
    }, displayTime);
}
function deleteItem(id) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/admin/items/delete/'+id, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                getItems();
                showMessage('Успешно' + xhr.responseText, 'Green');
            } else {
                showMessage('Товар имеет внешние связи' + xhr.responseText, 'red');
            }
            console.log(xhr.responseText);
        }
    };
    xhr.send();
}
function addOptionsToCategoryFilter(){
    let select = document.getElementById('categoryFilter');
    category.forEach(function(item) {
        let option = document.createElement('option');
        option.value = item.categoriesName;
        option.text = item.categoriesName;
        select.appendChild(option);
    });
    let option = document.createElement('option');
    option.value = 'null';
    option.text = 'Не выбрано';
    option.selected = true;
    select.appendChild(option);
}