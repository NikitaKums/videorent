package test.fujitsu.videostore.ui.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import test.fujitsu.videostore.ui.helpers.Helper;

public abstract class BaseFormImpl<DomainType, ViewLogicType extends BaseListLogic<DomainType>> extends Div implements BaseForm<DomainType> {

    protected DomainType entity;
    protected ViewLogicType viewLogic;
    protected Button save;
    protected Button delete;
    protected Button cancel;

    public BaseFormImpl(DomainType entity, ViewLogicType viewLogic){
        this.entity = entity;
        this.viewLogic = viewLogic;
    }

    protected void createSaveButton(){
        save = Helper.CreateButtonWithTextAndWidth("save", "Save", "100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    protected void createCancelButton(){
        cancel = Helper.CreateButtonWithTextAndWidth("cancel", "Cancel", "100%");
        cancel.addClickListener(event -> viewLogic.cancel());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancel())
                .setFilter("event.key == 'Escape'");
    }

    protected void createDeleteButton(){
        delete = Helper.CreateButtonWithTextAndWidth("delete", "Delete", "100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (entity != null) {
                viewLogic.deleteEntity(entity);
            }
        });
    }

}
