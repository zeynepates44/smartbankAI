package SmartBankAI.dto;

import java.util.List;

public class CustomerPageResponseDTO {
    private List<CustomerResponseDTO> customers;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public CustomerPageResponseDTO() {}

    public CustomerPageResponseDTO(List<CustomerResponseDTO> customers, int currentPage, int totalPages, long totalItems) {
        this.customers = customers;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<CustomerResponseDTO> getCustomers() { return customers; }
    public void setCustomers(List<CustomerResponseDTO> customers) { this.customers = customers; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
}