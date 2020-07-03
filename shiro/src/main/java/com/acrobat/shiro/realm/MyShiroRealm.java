package com.acrobat.shiro.realm;

import com.acrobat.shiro.dao.*;
import com.acrobat.shiro.entity.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author xutao
 * @date 2018-11-29 14:45
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;



    /**
     * 鉴权（获取用户权限信息，权限信息包括角色和权限）
     * 这个接口一定要缓存支持，否则用户每次请求都会查询数据库
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) super.getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 获得用户的角色集合
        Set<String> roles = getUserRolesByUsername(username);
        authorizationInfo.setRoles(roles);

        // 获得用户的权限集合
        Set<String> permissions = getUserPermissionsByUsername(username);
        authorizationInfo.addStringPermissions(permissions);

        return authorizationInfo;
    }


    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());

        // 根据输入用户名，查询用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
//        if (user.isLocked()) {
//            throw new LockedAccountException();
//        }

        // 使用用户的salt（随机串）进行MD5并设置到token，这里还能用Sha512Hash等加密
        String credentials = new Md5Hash(password, user.getSalt()).toString();
        token.setPassword(credentials.toCharArray());

        // 返回用户的认证信息
        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }


    private Set<String> getUserRolesByUsername(String username) {
        Set<String> resultSet = new HashSet<>();

        Map<Long, Role> roleMap = new HashMap<>();

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return resultSet;
        }
        long userId = user.getId();

        List<UserRole> userRoleList = userRoleMapper.listByUserId(userId);
        for (UserRole userRole : userRoleList) {
            long roleId = userRole.getRoleId();
            if (!roleMap.containsKey(roleId)) {
                Role role = roleMapper.selectByPrimaryKey(roleId);
                if (role != null) {
                    resultSet.add(role.getName());

                    roleMap.put(roleId, role);
                }
            }
        }

        return resultSet;
    }

    private Set<String> getUserPermissionsByUsername(String username) {
        Set<String> resultSet = new HashSet<>();

        Map<Long, Permission> permissionMap = new HashMap<>();

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return resultSet;
        }
        long userId = user.getId();

        Set<Long> roleIdSet = new HashSet<>();
        List<UserRole> userRoleList = userRoleMapper.listByUserId(userId);
        for (UserRole userRole : userRoleList) {
            roleIdSet.add(userRole.getRoleId());
        }

        // 遍历角色，获取所有角色的权限
        for (long roleId : roleIdSet) {
            List<RolePermission> rolePermissionList = rolePermissionMapper.listByRoleId(roleId);
            for (RolePermission rolePermission : rolePermissionList) {
                long permissionId = rolePermission.getPermissionId();
                if (!permissionMap.containsKey(permissionId)) {
                    Permission permission = permissionMapper.selectByPrimaryKey(permissionId);
                    if (permission != null) {
                        resultSet.add(permission.getPermission());

                        permissionMap.put(permissionId, permission);
                    }
                }
            }
        }

        return resultSet;
    }
}
