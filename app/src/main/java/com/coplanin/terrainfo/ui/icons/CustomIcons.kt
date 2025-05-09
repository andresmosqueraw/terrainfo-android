package com.coplanin.terrainfo.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val SearchIcon: ImageVector
    get() {
        if (_searchIcon != null) {
            return _searchIcon!!
        }
        _searchIcon = ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(784f, 840f)
                lineTo(532f, 588f)
                quadToRelative(-30f, 24f, -69f, 38f)
                reflectiveQuadToRelative(-83f, 14f)
                quadToRelative(-109f, 0f, -184.5f, -75.5f)
                reflectiveQuadTo(120f, 380f)
                reflectiveQuadToRelative(75.5f, -184.5f)
                reflectiveQuadTo(380f, 120f)
                reflectiveQuadToRelative(184.5f, 75.5f)
                reflectiveQuadTo(640f, 380f)
                quadToRelative(0f, 44f, -14f, 83f)
                reflectiveQuadToRelative(-38f, 69f)
                lineToRelative(252f, 252f)
                close()
                moveTo(380f, 560f)
                quadToRelative(75f, 0f, 127.5f, -52.5f)
                reflectiveQuadTo(560f, 380f)
                reflectiveQuadToRelative(-52.5f, -127.5f)
                reflectiveQuadTo(380f, 200f)
                reflectiveQuadToRelative(-127.5f, 52.5f)
                reflectiveQuadTo(200f, 380f)
                reflectiveQuadToRelative(52.5f, 127.5f)
                reflectiveQuadTo(380f, 560f)
            }
        }.build()
        return _searchIcon!!
    }

private var _searchIcon: ImageVector? = null


public val Person: ImageVector
    get() {
        if (_person != null) {
            return _person!!
        }
        _person = ImageVector.Builder(
            name = "Person",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 480f)
                quadToRelative(-66f, 0f, -113f, -47f)
                reflectiveQuadToRelative(-47f, -113f)
                reflectiveQuadToRelative(47f, -113f)
                reflectiveQuadToRelative(113f, -47f)
                reflectiveQuadToRelative(113f, 47f)
                reflectiveQuadToRelative(47f, 113f)
                reflectiveQuadToRelative(-47f, 113f)
                reflectiveQuadToRelative(-113f, 47f)
                moveTo(160f, 800f)
                verticalLineToRelative(-112f)
                quadToRelative(0f, -34f, 17.5f, -62.5f)
                reflectiveQuadTo(224f, 582f)
                quadToRelative(62f, -31f, 126f, -46.5f)
                reflectiveQuadTo(480f, 520f)
                reflectiveQuadToRelative(130f, 15.5f)
                reflectiveQuadTo(736f, 582f)
                quadToRelative(29f, 15f, 46.5f, 43.5f)
                reflectiveQuadTo(800f, 688f)
                verticalLineToRelative(112f)
                close()
                moveToRelative(80f, -80f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(-32f)
                quadToRelative(0f, -11f, -5.5f, -20f)
                reflectiveQuadTo(700f, 654f)
                quadToRelative(-54f, -27f, -109f, -40.5f)
                reflectiveQuadTo(480f, 600f)
                reflectiveQuadToRelative(-111f, 13.5f)
                reflectiveQuadTo(260f, 654f)
                quadToRelative(-9f, 5f, -14.5f, 14f)
                reflectiveQuadToRelative(-5.5f, 20f)
                close()
                moveToRelative(240f, -320f)
                quadToRelative(33f, 0f, 56.5f, -23.5f)
                reflectiveQuadTo(560f, 320f)
                reflectiveQuadToRelative(-23.5f, -56.5f)
                reflectiveQuadTo(480f, 240f)
                reflectiveQuadToRelative(-56.5f, 23.5f)
                reflectiveQuadTo(400f, 320f)
                reflectiveQuadToRelative(23.5f, 56.5f)
                reflectiveQuadTo(480f, 400f)
                moveToRelative(0f, 320f)
            }
        }.build()
        return _person!!
    }

private var _person: ImageVector? = null




public val EyeSlash: ImageVector
    get() {
        if (_eye_slash != null) {
            return _eye_slash!!
        }
        _eye_slash = ImageVector.Builder(
            name = "EyeSlash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.97993f, 8.22257f)
                curveTo(3.0568f, 9.3138f, 2.3524f, 10.596f, 1.9344f, 12.0015f)
                curveTo(3.2256f, 16.338f, 7.2431f, 19.5f, 11.9991f, 19.5f)
                curveTo(12.9917f, 19.5f, 13.9521f, 19.3623f, 14.8623f, 19.1049f)
                moveTo(6.22763f, 6.22763f)
                curveTo(7.8839f, 5.1356f, 9.8677f, 4.5f, 12f, 4.5f)
                curveTo(16.756f, 4.5f, 20.7734f, 7.662f, 22.0647f, 11.9985f)
                curveTo(21.3528f, 14.3919f, 19.8106f, 16.4277f, 17.772f, 17.772f)
                moveTo(6.22763f, 6.22763f)
                lineTo(3f, 3f)
                moveTo(6.22763f, 6.22763f)
                lineTo(9.87868f, 9.87868f)
                moveTo(17.772f, 17.772f)
                lineTo(21f, 21f)
                moveTo(17.772f, 17.772f)
                lineTo(14.1213f, 14.1213f)
                moveTo(14.1213f, 14.1213f)
                curveTo(14.6642f, 13.5784f, 15f, 12.8284f, 15f, 12f)
                curveTo(15f, 10.3431f, 13.6569f, 9f, 12f, 9f)
                curveTo(11.1716f, 9f, 10.4216f, 9.3358f, 9.8787f, 9.8787f)
                moveTo(14.1213f, 14.1213f)
                lineTo(9.87868f, 9.87868f)
            }
        }.build()
        return _eye_slash!!
    }

private var _eye_slash: ImageVector? = null

