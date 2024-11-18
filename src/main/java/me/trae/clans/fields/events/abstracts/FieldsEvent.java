package me.trae.clans.fields.events.abstracts;

import me.trae.clans.fields.FieldsBlock;
import me.trae.clans.fields.events.abstracts.interfaces.IFieldsEvent;
import me.trae.core.event.CustomEvent;

public class FieldsEvent extends CustomEvent implements IFieldsEvent {

    private final FieldsBlock fieldsBlock;

    public FieldsEvent(final FieldsBlock fieldsBlock) {
        this.fieldsBlock = fieldsBlock;
    }

    @Override
    public FieldsBlock getFieldsBlock() {
        return this.fieldsBlock;
    }
}