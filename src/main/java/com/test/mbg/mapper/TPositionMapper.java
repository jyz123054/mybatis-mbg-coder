package com.test.mbg.mapper;

import com.test.mbg.entity.TPosition;
import java.util.List;

public interface TPositionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_position
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_position
     *
     * @mbggenerated
     */
    int insert(TPosition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_position
     *
     * @mbggenerated
     */
    TPosition selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_position
     *
     * @mbggenerated
     */
    List<TPosition> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_position
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TPosition record);
}