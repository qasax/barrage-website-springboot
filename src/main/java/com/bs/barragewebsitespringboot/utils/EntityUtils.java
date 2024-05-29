package com.bs.barragewebsitespringboot.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>超级强大的<i>POJO</i>数据实体类转换工具</p>
 * <p>{@link EntityUtils}工具类用于基于Lambda表达式实现类型转换，具有如下优点：</p>
 * <p>1. 实现对象转对象；集合转集合；分页对象转分页对象</p>
 * <p>2. 实体类转Vo、实体类转DTO等都能应用此工具类</p>
 * <p>3. 转换参数均为不可变类型，业务更加安全</p>
 *
 * @author <a href="http://www.altitude.xin" target="_blank">赛泰先生</a>
 * @author <a href="https://gitee.com/decsa/ucode-cms-vue" target="_blank">UCode CMS</a>
 * @author <a href="https://space.bilibili.com/1936685014" target="_blank">B站视频</a>
 * @since 2019/06/19 17:23
 **/
public class EntityUtils {
    private EntityUtils() {
    }

    /**
     * 将对象集合按照一定规则映射后收集为另一种形式的集合
     *
     * @param <R>       最终结果的泛型
     * @param <S>       原始集合元素的类泛型
     * @param <T>       转换后元素的中间状态泛型
     * @param <A>       最终结果收集器泛型
     * @param source    最原始的集合实例
     * @param action    转换规则
     * @param collector 收集器的类型
     * @return 变换后存储新元素的集合实例
     */
    public static <R, S, T, A> R collectCommon(final Collection<S> source, Function<? super S, ? extends T> action, Collector<? super T, A, R> collector) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(collector);
        return source.stream().map(action).collect(collector);
    }

    /**
     * 将实体类对象从{@code T}类型转化为{@code R}类型
     * <pre>
     * public class Subject{
     *     private Integer subId;
     *     private String subName;
     *
     *     public Subject(Subject subject) {
     *         if (Objects.nonNull(subject)) {
     *             this.subId=subject.subId;
     *             this.subName=subject.subName;
     *         }
     *     }
     * }
     * public class SubjectBo extends Subject {
     *     private Integer score;
     *
     *     public SubjectBo(Subject subject) {
     *         super(subject);
     *     }
     * }
     * </pre>
     * 使用示例实现实体类Subject转换为SubjectBo
     * <pre>
     *     SubjectBo subjectBo = EntityUtils.toObj(subject, SubjectBo::new);
     * </pre>
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param obj    源对象实例
     * @param action 映射Lambda表达式 参数不能为<code>null</code> 否则抛出异常
     * @return 变换后的类型，如果source为null,则返回null
     */
    public static <T, R> R toObj(final T obj, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        return Optional.ofNullable(obj).map(action).orElse(null);
    }


    /**
     * <p>将{@code List}集合换成另一种类型</p>
     * <pre>
     *     public class User {
     *         private Long userId;
     *         private String userName;
     *         private String sex;
     *     }
     * </pre>
     * <p>通过方法引用获得任意列组成的新{@code List}集合</p>
     * <pre>
     *     List&lt;Long&gt; userIds = EntityUtils.toList(list,User::getUserId)
     * </pre>
     * <p>在{@code User}类中添加有如下构造器</p>
     * <pre>
     *     public User(User user) {
     *         if(user != null) {
     *             this.userId = user.userId;
     *             this.userName = user.userName;
     *             this.sex = user.sex;
     *         }
     *     }
     * </pre>
     * <pre>
     *     public class UserVo extends User {
     *         private String deptName;
     *
     *         public UserVo (User user) {
     *             super(user);
     *         }
     *     }
     * </pre>
     * 通过如下代码可实现DO 转 VO
     * <pre>
     *     List&lt;Long&gt; userVos = EntityUtils.toList(list,UserVo::new)
     * </pre>
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param list   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> List<R> toList(final Collection<T> list, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        if (Objects.nonNull(list)) {
            return list.stream().map(action).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * <p>将数组转化为集合</p>
     * 示例：不转换实体类类型
     *
     * @param arrays 数组实例
     * @param <T>    原始实体类类型
     * @return 以<code>R</code>为元素的集合实例
     */
    public static <T> List<T> toList(final T[] arrays) {
        return new ArrayList<>(Arrays.asList(arrays));
    }

    /**
     * <p>将数组转化为集合</p>
     * 示例一：不转换实体类类型
     * 示例二：转换实体类类型
     *
     * @param arrays 数组实例
     * @param action 值为{@code Function.identity()}不转变实体类类型
     * @param <T>    原始实体类类型
     * @param <R>    目标实体类类型
     * @return 以<code>R</code>为元素的集合实例
     */
    public static <T, R> List<R> toList(final T[] arrays, final Function<? super T, ? extends R> action) {
        return toList(Arrays.asList(arrays), action);
    }

    /**
     * <p>将以{@code T}类型为元素的集合转化成以以{@code R}类型为元素的集合 并允许过滤数据</p>
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param list   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> List<R> toList(final Collection<T> list, final Function<? super T, ? extends R> action, Predicate<R> pred) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(pred);
        if (Objects.nonNull(list)) {
            return list.stream().map(action).filter(pred).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    /**
     * 将IPaged对象以一种类型转换成另一种类型
     *
     * @param page   源Page
     * @param action 转换规则
     * @param <T>    源实体类
     * @param <R>    目标Page类型泛型
     * @return 变换后的分页类型
     */
    public static <T, R> IPage<R> toPage(IPage<T> page, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(page);
        Objects.requireNonNull(action);
        return page.convert(action);
    }






    /**
     * 将List集合以一种类型转换成Set集合
     *
     * @param <T>    源数据类型
     * @param <R>    变换后数据类型
     * @param data   源List集合
     * @param action 映射Lambda表达式
     * @return 变换后的类型集合，如果source为null,则返回空集合
     */
    public static <T, R> Set<R> toSet(final Collection<T> data, final Function<? super T, ? extends R> action) {
        Objects.requireNonNull(action);
        if (Objects.nonNull(data)) {
            return data.stream().map(action).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * <p>对集合中元素按照指定列进行分组</p>
     * <p>返回值是{@code Map}，其中Key为分组列，Value为当前元素</p>
     *
     * @param list    集合实例
     * @param gColumn 分组列（方法引用表示）
     * @param <E>     集合元素泛型
     * @param <G>     分组列数据类型泛型
     * @return {@code Map}实例
     * @since 1.6.0
     */
    public static <E, G> Map<G, List<E>> groupBy(final Collection<E> list, final Function<E, G> gColumn) {
        Objects.requireNonNull(gColumn);
        if (Objects.nonNull(list)) {
            return list.stream().collect(Collectors.groupingBy(gColumn));
        }
        return Collections.emptyMap();
    }

    /**
     * <p>对集合中元素按照指定列进行分组</p>
     * <p>返回值是{@code Map}，其中Key为分组列，Value为当前元素</p>
     * <p>通过比较器对Map中元素排序</p>
     *
     * @param list    集合实例
     * @param gColumn 分组列（方法引用表示）
     * @param <E>     集合元素泛型
     * @param <G>     分组列数据类型泛型
     * @return LinkedHashMap实例
     * @since 1.6.1
     */
    public static <E, G> Map<G, List<E>> groupBy(final Collection<E> list, final Function<E, G> gColumn, Comparator<Map.Entry<G, List<E>>> comparator) {
        Objects.requireNonNull(comparator);
        Map<G, List<E>> map = groupBy(list, gColumn);
        BinaryOperator<List<E>> binaryOperator = (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
        return map.entrySet().stream().sorted(comparator).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, binaryOperator, LinkedHashMap::new));
    }

    /**
     * <p>对集合中元素按照指定列进行分组</p>
     * <p>返回值是{@code Map}，其中Key为分组列</p>
     *
     * @param list    集合实例
     * @param gColumn 分组列（方法引用表示）
     * @param action  转换行为
     * @param <U>     Value集合元素类型泛型
     * @param <E>     集合元素泛型
     * @param <G>     分组列数据类型泛型
     * @return {@code Map}实例
     * @since 1.6.0
     */
    public static <E, G, U> Map<G, List<U>> groupBy(final Collection<E> list, final Function<E, G> gColumn, final Function<E, U> action) {
        Objects.requireNonNull(gColumn);
        if (Objects.nonNull(list)) {
            return list.stream().collect(Collectors.groupingBy(gColumn, Collectors.mapping(action, Collectors.toList())));
        }
        return Collections.emptyMap();
    }
}
