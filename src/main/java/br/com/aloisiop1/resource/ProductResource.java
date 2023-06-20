package br.com.aloisiop1.resource;

import br.com.aloisiop1.dto.Product;
import br.com.aloisiop1.response.Result;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.Set;

@Path("/product")
public class ProductResource {
    @Inject
    Validator validator;

    @POST
    public Result addProduct(@Valid Product product){
        return  new Result("Produto inserido é válido");
    }

    @POST
    @Path("/no-validation")
    public Result addProductNoValidation(Product product){
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if(violations.isEmpty()){
            return  new Result("Produto inserido é válido");
        }else{
         return new Result(violations);
        }
    }

}
