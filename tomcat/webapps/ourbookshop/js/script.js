document.addEventListener("DOMContentLoaded", function () {
    loadBooks();
});

function loadBooks(genre = '') {
    fetch(`HomeServlet?genre=${genre}`)
        .then(response => response.json())
        .then(data => {
            const bookList = document.getElementById("book-list");
            bookList.innerHTML = ""; // Clear previous books

            data.forEach(book => {
                const bookItem = document.createElement("div");
                bookItem.classList.add("book-item");

                bookItem.innerHTML = `
                    <img src="${book.image_url}" alt="${book.title}">
                    <h3 class="book-title">${book.title}</h3>
                    <p class="book-author">${book.author}</p>
                    <p class="book-price">$${book.price}</p>
                    <button class="add-to-cart" onclick="addToCart(${book.book_id})">+ Add to Cart</button>
                `;

                bookList.appendChild(bookItem);
            });
        })
        .catch(error => console.error("Error fetching books:", error));
}

function filterGenre(genre) {
    loadBooks(genre);
}

function addToCart(bookId) {
    alert("Book " + bookId + " added to cart!");
}
