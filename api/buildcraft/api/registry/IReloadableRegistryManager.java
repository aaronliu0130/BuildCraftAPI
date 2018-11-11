package buildcraft.api.registry;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import buildcraft.api.registry.IReloadableRegistry.PackType;

public interface IReloadableRegistryManager {

    PackType getType();

    void reload(IReloadableRegistry<?> registry);

    void reload(IReloadableRegistry<?>... registries);

    void reload(Set<IReloadableRegistry<?>> registries);

    /** @return True if any of the calls to reload ({@link #reload(IReloadableRegistry) a},
     *         {@link #reload(IReloadableRegistry...) b}, or {@link #reload(Set) c}) haven't completed yet. */
    boolean isInReload();

    /*** @return The number of times that this reload manager has reloaded any of the registries this contains. Note
     *         that this might be negative if the value has overflowed. This is intended to be used for checking if this
     *         registry has reloaded while something cached a recipe, so that the cache can be cleared. This is a fairly
     *         cheap way to see if a reload had happened, especially as reload listeners might be temporary (for example
     *         tile entities or pipes). */
    int getReloadCount();

    /** @return All registries that this manager tracks. */
    Map<String, IReloadableRegistry<?>> getAllRegistries();

    @Nullable
    <R> IReloadableRegistry<R> getRegistry(Class<R> recipeClass);

    /** Creates and registers a basic {@link IReloadableRegistry}.
     * 
     * @param name The name for the given registry. This should be unique, and the best way of doing that is to prefix
     *            it with the modid, followed by a slash, and then the mod-unique name.
     * @throws IllegalArgumentException if name contains a colon. */
    <R> IReloadableRegistry<R> createRegistry(String name, Class<R> clazz);

    /** Scripts will be loaded from "[pack_type.prefix]/[pack_name]/compat/[entry_path]/". The entryPath parameter
     * should start with the modid.
     * 
     * @param entryPath */
    <R> IScriptableRegistry<R> createScriptableRegistry(String entryPath, Class<R> clazz);

    void registerRegistry(String entryType, IScriptableRegistry<?> registry);

    /** Delegates to {@link #registerRegistry(String, IScriptableRegistry)}, but uses
     * {@link IScriptableRegistry#getEntryType()} as the key. */
    default void registerRegistry(IScriptableRegistry<?> registry) {
        registerRegistry(registry.getEntryType(), registry);
    }
}