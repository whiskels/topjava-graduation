package com.whiskels.graduation.web.user;

import com.whiskels.graduation.HasId;
import com.whiskels.graduation.model.User;
import com.whiskels.graduation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

import static com.whiskels.graduation.util.ValidationUtil.assureIdConsistent;
import static com.whiskels.graduation.util.ValidationUtil.checkNew;


@Slf4j
public abstract class AbstractUserController {

    // Validate manually cause UniqueMailValidator doesn't work for update with user.id==null
    private WebDataBinder binder;

    @Autowired
    protected UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() != null && emailValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(emailValidator);
            this.binder = binder;
        }
    }

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    protected void checkAndValidateForUpdate(HasId user, int id) throws BindException {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }

    public void update(User user, int id) throws BindException {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }

    public User getWithVotes(int id) {
        log.info("getWithMeals {}", id);
        return service.getWithVotes(id);
    }
}
