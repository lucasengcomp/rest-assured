package rest;

import classe.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbosHTTPTest {

    String urlPath = "https://restapi.wcaquino.me/users";

    @Test
    public void deveSalvarUsuario() {
        given()
            .log().all()
                .contentType("application/json")
                .body("{ \"name\" : \"Lucas\", \"age\" : 19 }")
        .when()
            .post(urlPath)
        .then()
            .log().all()
            .statusCode(201);
    }

    @Test
    public void deveSalvarUsuarioEValidarCampos() {
        given()
            .log().all()
                .contentType("application/json")
                .body("{ \"name\" : \"Lucas\", \"age\" : 19 }")
        .when()
            .post(urlPath)
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Lucas"))
            .body("age", is(19));
    }

    @Test
    public void deveTentarSalvarUsuarioSemNomeEDarErro() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"age\" : 19 }")
        .when()
            .post(urlPath)
        .then()
            .log().all()
            .statusCode(400)
            .body("id", is(nullValue()))
            .body("error", is("Name é um atributo obrigatório"));
    }

    @Test
    public void deveAlterarUsuario() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\" : \"Lucas Foi Alterado\", \"age\" : 21 }")
        .when()
            .put(urlPath + "/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Lucas Foi Alterado"))
            .body("age", is(21));
    }


    @Test
    public void deveSalvarUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<>();

        params.put("name", "Batman Via Map");
        params.put("age", 30);

        given()
                .log().all()
                .contentType("application/json")
                .body(params)
                .when()
                .post(urlPath)
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Batman Via Map"))
                .body("age", is(30));
    }

//    @Test
//    public void deveAlterarUsuarioComUrlDinamica() {
//        given()
//            .log().all()
//            .contentType("application/json")
//            .body("{ \"name\" : \"Lucas Foi Alterado\", \"age\" : 21 }")
//        .when()
//            .put(urlPath + "https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
//        .then()
//            .log().all()
//            .statusCode(200)
//            .body("id", is(1))
//            .body("name", is("Lucas Foi Alterado"))
//            .body("age", is(21));
//    }
//
//
//    @Test
//    public void deveAlterarUsuarioComUrlDinamicaComPathParams() {
//        given()
//            .log().all()
//            .contentType("application/json")
//            .body("{ \"name\" : \"Lucas Foi Alterado\", \"age\" : 21 }")
//            .pathParam("entidade", "users")
//            .pathParam("userId", "1")
//        .when()
//            .put(urlPath + "https://restapi.wcaquino.me/{entidade}/{userId}")
//        .then()
//            .log().all()
//            .statusCode(200)
//            .body("id", is(1))
//            .body("name", is("Lucas Foi Alterado"))
//            .body("age", is(21));
//    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto() {
        User user = new User("Usuário via objeto", 20);

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post(urlPath)
                .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuário via objeto"))
                .body("age", is(20));
    }

    @Test
    public void deveSalvarUsuarioDesserializado() {
        User user = new User("Usuário desserializado", 20);

        User usuarioInserido = given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post(urlPath)
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().as(User.class);

        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario desserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(20));
    }

    @Test
    public void deveRemoverUmUsuario()  {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(204);
    }

    @Test
    public void deveTentarRemoverUsuarioInexistenteEDarErro()  {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/999")
        .then()
            .log().all()
            .statusCode(400)
                .body("error", is("Registro inexistente"));
    }
}

