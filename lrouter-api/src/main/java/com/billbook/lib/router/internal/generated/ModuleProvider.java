package com.billbook.lib.router.internal.generated;

import com.billbook.lib.router.internal.AModuleContainer;
import com.billbook.lib.router.internal.BModuleContainer;
import com.billbook.lib.router.internal.ModuleContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xluotong@gmail.com
 */
public class ModuleProvider {

    private ModuleProvider() { }

    public static List<? extends ModuleContainer> modules() {
//        throw new RuntimeException("Stub!");
        List<ModuleContainer> containers = new ArrayList<>();
        containers.add(new AModuleContainer());
        containers.add(new BModuleContainer());
        containers.add(new AModuleContainer());
        containers.add(new BModuleContainer());
        return containers;
    }
}
