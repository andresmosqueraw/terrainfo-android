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



public val Eye: ImageVector
    get() {
        if (_eye != null) {
            return _eye!!
        }
        _eye = ImageVector.Builder(
            name = "Eye",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16f, 8f)
                reflectiveCurveToRelative(-3f, -5.5f, -8f, -5.5f)
                reflectiveCurveTo(0f, 8f, 0f, 8f)
                reflectiveCurveToRelative(3f, 5.5f, 8f, 5.5f)
                reflectiveCurveTo(16f, 8f, 16f, 8f)
                moveTo(1.173f, 8f)
                arcToRelative(13f, 13f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.66f, -2.043f)
                curveTo(4.12f, 4.668f, 5.88f, 3.5f, 8f, 3.5f)
                reflectiveCurveToRelative(3.879f, 1.168f, 5.168f, 2.457f)
                arcTo(13f, 13f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.828f, 8f)
                quadToRelative(-0.086f, 0.13f, -0.195f, 0.288f)
                curveToRelative(-0.335f, 0.48f, -0.83f, 1.12f, -1.465f, 1.755f)
                curveTo(11.879f, 11.332f, 10.119f, 12.5f, 8f, 12.5f)
                reflectiveCurveToRelative(-3.879f, -1.168f, -5.168f, -2.457f)
                arcTo(13f, 13f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.172f, 8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 5.5f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 0f, 5f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -5f)
                moveTo(4.5f, 8f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7f, 0f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -7f, 0f)
            }
        }.build()
        return _eye!!
    }

private var _eye: ImageVector? = null


public val User: ImageVector
    get() {
        if (_user != null) {
            return _user!!
        }
        _user = ImageVector.Builder(
            name = "Account",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16f, 7.992f)
                curveTo(16f, 3.58f, 12.416f, 0f, 8f, 0f)
                reflectiveCurveTo(0f, 3.58f, 0f, 7.992f)
                curveToRelative(0f, 2.43f, 1.104f, 4.62f, 2.832f, 6.09f)
                curveToRelative(0.016f, 0.016f, 0.032f, 0.016f, 0.032f, 0.032f)
                curveToRelative(0.144f, 0.112f, 0.288f, 0.224f, 0.448f, 0.336f)
                curveToRelative(0.08f, 0.048f, 0.144f, 0.111f, 0.224f, 0.175f)
                arcTo(7.98f, 7.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.016f, 16f)
                arcToRelative(7.98f, 7.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.48f, -1.375f)
                curveToRelative(0.08f, -0.048f, 0.144f, -0.111f, 0.224f, -0.16f)
                curveToRelative(0.144f, -0.111f, 0.304f, -0.223f, 0.448f, -0.335f)
                curveToRelative(0.016f, -0.016f, 0.032f, -0.016f, 0.032f, -0.032f)
                curveToRelative(1.696f, -1.487f, 2.8f, -3.676f, 2.8f, -6.106f)
                close()
                moveToRelative(-8f, 7.001f)
                curveToRelative(-1.504f, 0f, -2.88f, -0.48f, -4.016f, -1.279f)
                curveToRelative(0.016f, -0.128f, 0.048f, -0.255f, 0.08f, -0.383f)
                arcToRelative(4.17f, 4.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.416f, -0.991f)
                curveToRelative(0.176f, -0.304f, 0.384f, -0.576f, 0.64f, -0.816f)
                curveToRelative(0.24f, -0.24f, 0.528f, -0.463f, 0.816f, -0.639f)
                curveToRelative(0.304f, -0.176f, 0.624f, -0.304f, 0.976f, -0.4f)
                arcTo(4.15f, 4.15f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 10.342f)
                arcToRelative(4.185f, 4.185f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.928f, 1.166f)
                curveToRelative(0.368f, 0.368f, 0.656f, 0.8f, 0.864f, 1.295f)
                curveToRelative(0.112f, 0.288f, 0.192f, 0.592f, 0.24f, 0.911f)
                arcTo(7.03f, 7.03f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 14.993f)
                close()
                moveToRelative(-2.448f, -7.4f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.208f, -1.024f)
                curveToRelative(0f, -0.351f, 0.064f, -0.703f, 0.208f, -1.023f)
                curveToRelative(0.144f, -0.32f, 0.336f, -0.607f, 0.576f, -0.847f)
                curveToRelative(0.24f, -0.24f, 0.528f, -0.431f, 0.848f, -0.575f)
                curveToRelative(0.32f, -0.144f, 0.672f, -0.208f, 1.024f, -0.208f)
                curveToRelative(0.368f, 0f, 0.704f, 0.064f, 1.024f, 0.208f)
                curveToRelative(0.32f, 0.144f, 0.608f, 0.336f, 0.848f, 0.575f)
                curveToRelative(0.24f, 0.24f, 0.432f, 0.528f, 0.576f, 0.847f)
                curveToRelative(0.144f, 0.32f, 0.208f, 0.672f, 0.208f, 1.023f)
                curveToRelative(0f, 0.368f, -0.064f, 0.704f, -0.208f, 1.023f)
                arcToRelative(2.84f, 2.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.576f, 0.848f)
                arcToRelative(2.84f, 2.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.848f, 0.575f)
                arcToRelative(2.715f, 2.715f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.064f, 0f)
                arcToRelative(2.84f, 2.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.848f, -0.575f)
                arcToRelative(2.526f, 2.526f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.56f, -0.848f)
                close()
                moveToRelative(7.424f, 5.306f)
                curveToRelative(0f, -0.032f, -0.016f, -0.048f, -0.016f, -0.08f)
                arcToRelative(5.22f, 5.22f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.688f, -1.406f)
                arcToRelative(4.883f, 4.883f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.088f, -1.135f)
                arcToRelative(5.207f, 5.207f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.04f, -0.608f)
                arcToRelative(2.82f, 2.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.464f, -0.383f)
                arcToRelative(4.2f, 4.2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.624f, -0.784f)
                arcToRelative(3.624f, 3.624f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.528f, -1.934f)
                arcToRelative(3.71f, 3.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.288f, -1.47f)
                arcToRelative(3.799f, 3.799f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.816f, -1.199f)
                arcToRelative(3.845f, 3.845f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.2f, -0.8f)
                arcToRelative(3.72f, 3.72f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.472f, -0.287f)
                arcToRelative(3.72f, 3.72f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.472f, 0.288f)
                arcToRelative(3.631f, 3.631f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.2f, 0.815f)
                arcToRelative(3.84f, 3.84f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.8f, 1.199f)
                arcToRelative(3.71f, 3.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.288f, 1.47f)
                curveToRelative(0f, 0.352f, 0.048f, 0.688f, 0.144f, 1.007f)
                curveToRelative(0.096f, 0.336f, 0.224f, 0.64f, 0.4f, 0.927f)
                curveToRelative(0.16f, 0.288f, 0.384f, 0.544f, 0.624f, 0.784f)
                curveToRelative(0.144f, 0.144f, 0.304f, 0.271f, 0.48f, 0.383f)
                arcToRelative(5.12f, 5.12f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.04f, 0.624f)
                curveToRelative(-0.416f, 0.32f, -0.784f, 0.703f, -1.088f, 1.119f)
                arcToRelative(4.999f, 4.999f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.688f, 1.406f)
                curveToRelative(-0.016f, 0.032f, -0.016f, 0.064f, -0.016f, 0.08f)
                curveTo(1.776f, 11.636f, 0.992f, 9.91f, 0.992f, 7.992f)
                curveTo(0.992f, 4.14f, 4.144f, 0.991f, 8f, 0.991f)
                reflectiveCurveToRelative(7.008f, 3.149f, 7.008f, 7.001f)
                arcToRelative(6.96f, 6.96f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.032f, 4.907f)
                close()
            }
        }.build()
        return _user!!
    }

private var _user: ImageVector? = null


public val ArrowBack: ImageVector
    get() {
        if (_arrow_back != null) {
            return _arrow_back!!
        }
        _arrow_back = ImageVector.Builder(
            name = "Arrow_back",
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
                moveTo(313f, 520f)
                lineToRelative(224f, 224f)
                lineToRelative(-57f, 56f)
                lineToRelative(-320f, -320f)
                lineToRelative(320f, -320f)
                lineToRelative(57f, 56f)
                lineToRelative(-224f, 224f)
                horizontalLineToRelative(487f)
                verticalLineToRelative(80f)
                close()
            }
        }.build()
        return _arrow_back!!
    }

private var _arrow_back: ImageVector? = null



public val Pencil: ImageVector
    get() {
        if (_pencil != null) {
            return _pencil!!
        }
        _pencil = ImageVector.Builder(
            name = "Pencil",
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
                moveTo(16.8617f, 4.48667f)
                lineTo(18.5492f, 2.79917f)
                curveTo(19.2814f, 2.0669f, 20.4686f, 2.0669f, 21.2008f, 2.7992f)
                curveTo(21.9331f, 3.5314f, 21.9331f, 4.7186f, 21.2008f, 5.4508f)
                lineTo(6.83218f, 19.8195f)
                curveTo(6.3035f, 20.3481f, 5.6514f, 20.7368f, 4.9349f, 20.9502f)
                lineTo(2.25f, 21.75f)
                lineTo(3.04978f, 19.0651f)
                curveTo(3.2632f, 18.3486f, 3.6519f, 17.6965f, 4.1805f, 17.1678f)
                lineTo(16.8617f, 4.48667f)
                close()
                moveTo(16.8617f, 4.48667f)
                lineTo(19.5f, 7.12499f)
            }
        }.build()
        return _pencil!!
    }

private var _pencil: ImageVector? = null



public val Trash: ImageVector
    get() {
        if (_trash != null) {
            return _trash!!
        }
        _trash = ImageVector.Builder(
            name = "Trash",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5.5f, 5.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 6f)
                verticalLineToRelative(6f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 0f)
                verticalLineTo(6f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.5f, -0.5f)
                moveToRelative(2.5f, 0f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.5f, 0.5f)
                verticalLineToRelative(6f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 0f)
                verticalLineTo(6f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.5f, -0.5f)
                moveToRelative(3f, 0.5f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1f, 0f)
                verticalLineToRelative(6f)
                arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.5f, 3f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 1f)
                horizontalLineTo(13f)
                verticalLineToRelative(9f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 2f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, -2f)
                verticalLineTo(4f)
                horizontalLineToRelative(-0.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, -1f)
                verticalLineTo(2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, -1f)
                horizontalLineTo(6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, -1f)
                horizontalLineToRelative(2f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                horizontalLineToRelative(3.5f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, 1f)
                close()
                moveTo(4.118f, 4f)
                lineTo(4f, 4.059f)
                verticalLineTo(13f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, 1f)
                horizontalLineToRelative(6f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1f, -1f)
                verticalLineTo(4.059f)
                lineTo(11.882f, 4f)
                close()
                moveTo(2.5f, 3f)
                horizontalLineToRelative(11f)
                verticalLineTo(2f)
                horizontalLineToRelative(-11f)
                close()
            }
        }.build()
        return _trash!!
    }

private var _trash: ImageVector? = null

public val Plus: ImageVector
    get() {
        if (_plus != null) {
            return _plus!!
        }
        _plus = ImageVector.Builder(
            name = "Plus",
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
                moveTo(12f, 4.5f)
                verticalLineTo(19.5f)
                moveTo(19.5f, 12f)
                lineTo(4.5f, 12f)
            }
        }.build()
        return _plus!!
    }

private var _plus: ImageVector? = null


