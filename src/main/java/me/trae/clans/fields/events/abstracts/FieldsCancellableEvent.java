package me.trae.clans.fields.events.abstracts;

import me.trae.clans.fields.data.FieldsBlock;
import me.trae.clans.fields.events.abstracts.interfaces.IFieldsEvent;
import me.trae.core.event.CustomCancellableEvent;

public class FieldsCancellableEvent extends CustomCancellableEvent implements IFieldsEvent {

    private final FieldsBlock fieldsBlock;

    public FieldsCancellableEvent(final FieldsBlock fieldsBlock) {
        this.fieldsBlock = fieldsBlock;
    }

    @Override
    public FieldsBlock getFieldsBlock() {
        return this.fieldsBlock;
    }
}