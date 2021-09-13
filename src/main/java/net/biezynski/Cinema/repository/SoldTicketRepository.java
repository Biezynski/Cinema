package net.biezynski.Cinema.repository;

import net.biezynski.Cinema.model.SoldTicket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SoldTicketRepository extends CrudRepository<SoldTicket, Long> {

    @Query(value = "SELECT * FROM SOLD_TICKET  ORDER BY DISCOUNT DESC", nativeQuery = true)
    List<SoldTicket> findAllOrderByDiscount();

}
