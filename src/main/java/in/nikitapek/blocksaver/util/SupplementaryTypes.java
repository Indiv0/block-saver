package in.nikitapek.blocksaver.util;

import com.amshulman.mbapi.storage.TypeSafeDistributedStorageMap;
import com.amshulman.mbapi.storage.TypeSafeUnifiedStorageMap;
import com.amshulman.typesafety.util.ParameterizedTypeImpl;
import com.google.gson.reflect.TypeToken;

import in.nikitapek.blocksaver.serialization.PlayerInfo;
import in.nikitapek.blocksaver.serialization.Reinforcement;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("rawtypes")
public final class SupplementaryTypes {
    public static final Type HASHSET = new TypeToken<HashSet>() {
    }.getType();
    public static final Type HASHMAP = new TypeToken<HashMap>() {
    }.getType();

    public static final Type FALLING_BLOCK = new TypeToken<FallingBlock>() {
    }.getType();
    public static final Type LIST = new TypeToken<List<?>>() {
    }.getType();
    public static final Type LOCATION = new TypeToken<Location>() {
    }.getType();
    public static final Type MATERIAL = new TypeToken<Material>() {
    }.getType();
    public static final Type PLAYER_INFO = new TypeToken<PlayerInfo>() {
    }.getType();
    public static final Type REINFORCEMENT = new TypeToken<Reinforcement>() {
    }.getType();
    public static final Type STRING = new TypeToken<String>() {
    }.getType();
    public static final Type TYPE_SAFE_DISTRIBUTED_STORAGE_MAP = new TypeToken<TypeSafeDistributedStorageMap>() {
    }.getType();

    public static final Type REINFORCEMENT_STORAGE;

    static {
        ParameterizedTypeImpl t = new ParameterizedTypeImpl(new TypeToken<TypeSafeUnifiedStorageMap>() {
        }.getType());
        t.addParamType(new TypeToken<Location>() {
        }.getType());
        t.addParamType(new TypeToken<Reinforcement>() {
        }.getType());

        REINFORCEMENT_STORAGE = t;
    }

    private SupplementaryTypes() {
    }
}
