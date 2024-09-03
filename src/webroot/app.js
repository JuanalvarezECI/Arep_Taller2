document.getElementById('addItemForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const itemInput = document.getElementById('itemInput');
    const itemList = document.getElementById('itemList');

    const newItem = document.createElement('li');

    const textContainer = document.createElement('span');
    textContainer.textContent = itemInput.value;
    newItem.appendChild(textContainer);

    const editButton = document.createElement('button');
    editButton.textContent = 'Edit';
    editButton.classList.add('edit');
    editButton.addEventListener('click', function() {
        const newValue = prompt('Enter new value', textContainer.textContent);
        if (newValue) {
            textContainer.textContent = newValue;
        }
    });
    newItem.appendChild(editButton);

    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', function() {
        itemList.removeChild(newItem);
    });
    newItem.appendChild(deleteButton);

    const upButton = document.createElement('button');
    upButton.textContent = 'Up';
    upButton.classList.add('up');
    upButton.addEventListener('click', function() {
        const prevSibling = newItem.previousElementSibling;
        if (prevSibling) {
            itemList.insertBefore(newItem, prevSibling);
        }
    });
    newItem.appendChild(upButton);

    const downButton = document.createElement('button');
    downButton.textContent = 'Down';
    downButton.classList.add('down');
    downButton.addEventListener('click', function() {
        const nextSibling = newItem.nextElementSibling;
        if (nextSibling) {
            itemList.insertBefore(nextSibling, newItem);
        }
    });
    newItem.appendChild(downButton);

    itemList.appendChild(newItem);
    itemInput.value = '';
});