package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "collectionrecipes")
@IdClass(CollectionRecipeId.class)
public class CollectionRecipe {

    @Id
    @Column(name = "collection_id")
    private Long collection_id;

    @Id
    @Column(name = "recipe_id")
    private Long recipe_id;

    @Column(name = "order_index")
    private Integer order_index;

    public Long getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Long collection_id) {
        this.collection_id = collection_id;
    }

    public Long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Long recipe_id) {
        this.recipe_id = recipe_id;
    }

    public Integer getOrder_index() {
        return order_index;
    }

    public void setOrder_index(Integer order_index) {
        this.order_index = order_index;
    }
}
