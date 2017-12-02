package agh.cs.oop.project1.commandargs;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CommandArgs {
    public final String sourceFilePath;
    public final boolean showTableOfContent;
    public final @Nullable String query;

    public CommandArgs(String sourceFilePath, boolean showTableOfContent, @Nullable String query) {
        this.sourceFilePath = sourceFilePath;
        this.showTableOfContent = showTableOfContent;
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandArgs that = (CommandArgs) o;
        return showTableOfContent == that.showTableOfContent &&
                Objects.equals(sourceFilePath, that.sourceFilePath) &&
                Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFilePath, showTableOfContent, query);
    }

    @Override
    public String toString() {
        return "CommandArgs{" +
                "sourceFilePath='" + sourceFilePath + '\'' +
                ", showTableOfContent=" + showTableOfContent +
                ", query='" + query + '\'' +
                '}';
    }
}
