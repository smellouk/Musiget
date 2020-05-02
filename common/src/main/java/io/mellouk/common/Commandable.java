package io.mellouk.common;

import io.mellouk.common.base.BaseCommand;

public interface Commandable<Command extends BaseCommand> {
    void onCommand(final Command command);
}
