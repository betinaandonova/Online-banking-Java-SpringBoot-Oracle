document.addEventListener("DOMContentLoaded", function () {
    const tables = document.querySelectorAll(".paginated-table");

    tables.forEach((table) => {
        const pagination = table.closest(".table-wrapper")?.nextElementSibling;

        if (!pagination || !pagination.classList.contains("pagination")) {
            return;
        }

        paginateTable(table, pagination, 10);
    });
});

function paginateTable(table, pagination, rowsPerPage) {
    const tbody = table.querySelector("tbody");

    if (!tbody) {
        return;
    }

    const rows = Array.from(tbody.querySelectorAll("tr"));

    if (rows.length <= rowsPerPage) {
        pagination.style.display = "none";
        return;
    }

    let currentPage = 1;
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    function showPage(page) {
        currentPage = page;

        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = index >= start && index < end ? "" : "none";
        });

        renderPagination();
    }

    function renderPagination() {
        pagination.innerHTML = "";

        const prevButton = document.createElement("button");
        prevButton.textContent = "‹";
        prevButton.disabled = currentPage === 1;
        prevButton.onclick = () => showPage(currentPage - 1);
        pagination.appendChild(prevButton);

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement("button");
            pageButton.textContent = String(i);
            if (i === currentPage) {
                pageButton.classList.add("active-page");
            }

            pageButton.onclick = () => showPage(i);
            pagination.appendChild(pageButton);
        }

        const nextButton = document.createElement("button");
        nextButton.textContent = "›";
        nextButton.disabled = currentPage === totalPages;
        nextButton.onclick = () => showPage(currentPage + 1);
        pagination.appendChild(nextButton);
    }

    showPage(1);
}