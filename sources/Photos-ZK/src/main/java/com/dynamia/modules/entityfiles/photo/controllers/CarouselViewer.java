package com.dynamia.modules.entityfiles.photo.controllers;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import com.dynamia.modules.entityfile.domain.EntityImage;
import com.dynamia.modules.entityfile.service.EntityFileService;
import com.dynamia.modules.entityfiles.photo.ImageListener;
import com.dynamia.tools.domain.AbstractEntity;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.util.ZKUtil;
import java.util.LinkedList;
import java.util.List;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.XulElement;

/**
 *
 * @author ronald
 */
public class CarouselViewer {

    private List<EntityImage> imagesCarouselables = new LinkedList<EntityImage>();
    private EntityImage imagesCarouselablesInit;
    private Image imagenPrincipal;
    private Hlayout rullet;
    private Label observ;
    private Boolean editable=true;
    private ImageListener imageListener;
    private EntityFileService entityFileService;
    private Integer widthMini = 60;
    private Integer heightMini = 60;
    private Integer width = 600;
    private Integer height = 400;
    private AbstractEntity entity;

    public CarouselViewer() {
        entityFileService = Containers.get().findObject(EntityFileService.class);
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }


    /*
     * public static void view(String titulo, AbstractEntity entity) { if
     * (titulo == null) { titulo = "Imagenes"; }
     * ZKUtil.showDialog("carouselViewer.zul", titulo, entity, "600px",
     * "900px"); }
     *
     * public static void view(String titulo, ImageListener imageListener,
     * AbstractEntity entity) { if (titulo == null) { titulo = "Imagenes"; }
     * //ZKUtil.showDialog("carouselViewer.zul", titulo, images, "600px",
     * "900px"); HashMap hashMap = new HashMap(); hashMap.put("imageListener",
     * imageListener); PhotoUtil.openDialog("carouselViewer.zul", titulo,
     * entity, "600px", "900px", hashMap); }
     */
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeightMini() {
        return heightMini;
    }

    public void setHeightMini(Integer heightMini) {
        this.heightMini = heightMini;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWidthMini() {
        return widthMini;
    }

    public void setWidthMini(Integer widthMini) {
        this.widthMini = widthMini;
    }

    public void loadAll() {
        setImagesCarouselablesInit(null);
        if (getEntity() == null) {
            imagesCarouselables = new LinkedList<EntityImage>();
        } else {
            imagesCarouselables = entityFileService.getEntityImages(getEntity());
        }
        for (EntityImage entityImage : imagesCarouselables) {
            entityImage.loadImage();
        }
        System.out.println("SIZE: " + imagesCarouselables.size());
        rullet.getChildren().clear();
        if (editable) {
            try {
                Div div = new Div();
                div.setWidth("60px");
                div.setHeight("60px");
                div.setAlign("center");
                div.setStyle("border: solid 1px gray; padding: 5px;vertical-align:middle;");

                Image image = new Image();
                image.setSrc("/zkau/web/frontera/img/add.png");
                image.setWidth("60px");
                image.setHeight("60px");
                image.addEventListener(Events.ON_CLICK, new EventListener() {

                    @Override
                    public void onEvent(Event t) throws Exception {
                        imageListener.onAdd();
                        //ImageCarouselable imagesCarouselabl=new ImageCarouselable

                    }
                });
                image.setParent(div);
                div.setParent(rullet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        for (final EntityImage entityImage : imagesCarouselables) {

            try {
                if (i == 0) {
                    imagesCarouselablesInit = entityImage;
                }
                Div div = new Div();
                div.setWidth("60px");
                div.setHeight("60px");
                div.setAlign("center");
                div.setStyle("border: solid 1px gray; padding: 5px;vertical-align:middle;");

                Image image = new Image();
                System.out.println("I1: " + entityImage.getName());
                System.out.println("I2: " + entityImage.getThumbnail());
                System.out.println("I2: " + entityImage.getThumbnail());
                org.zkoss.image.Image imagen = new AImage(entityImage.getName(), entityImage.getThumbnail());
                image.setContent(imagen);
                load(image, imagen, getWidthMini(), getHeightMini());
                //image.setWidth(getWidthMini() + "px");
                //image.setHeight(getHeightMini() + "px");
                image.addEventListener(Events.ON_CLICK, new EventListener() {

                    @Override
                    public void onEvent(Event t) throws Exception {
                        setImagesCarouselablesInit(entityImage);
                    }
                });

                image.setParent(div);
                div.setParent(rullet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        setImagesCarouselablesInit(imagesCarouselablesInit);
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public void setImagesCarouselablesInit(EntityImage entityImageInit) {
        this.imagesCarouselablesInit = entityImageInit;
        if (entityImageInit != null) {
            try {
                org.zkoss.image.Image image = new AImage(entityImageInit.getName(), entityImageInit.getFile());
                imagenPrincipal.setContent(image);
                load(imagenPrincipal, image, getWidth(), getHeight());
                //imagenPrincipal.setWidth(getWidth() + "px");
                //imagenPrincipal.setHeight(getHeight() + "px");
                observ.setValue(entityImageInit.getDescription());
            } catch (Exception e) {
            }
        }else{
            imagenPrincipal.setContent((org.zkoss.image.Image)null);
        }
    }

    public void load(XulElement component, org.zkoss.image.Image image, int maxWidhtMini, int maxHeightMini) {
        try {
            int widthR = image.getWidth();
            int heightR = image.getHeight();

            //MINI
            Integer difW = maxWidhtMini - widthR;
            Integer difH = maxHeightMini - heightR;
            Integer widthMiniFinal;
            Integer heightMiniFinal;
            /*
             * if (difW < 0) { difW = difW * -1; } if (difH < 0) { difH = difH *
             * -1; }
             */
            if (difW < difH) {
                if ((maxWidhtMini - widthR) >= 0) {
                    widthMiniFinal = widthR;
                } else {
                    widthMiniFinal = maxWidhtMini;
                }
                heightMiniFinal = ((heightR * widthMiniFinal) / widthR);

            } else {
                if ((maxHeightMini - heightR) >= 0) {
                    heightMiniFinal = heightR;
                } else {
                    heightMiniFinal = maxHeightMini;
                }
                widthMiniFinal = ((widthR * heightMiniFinal) / heightR);
            }

            component.setHeight(heightMiniFinal + "px");
            component.setWidth(widthMiniFinal + "px");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
     * public void load(XulElement component, org.zkoss.image.Image image, int
     * maxWidht, int maxHeight) { try { int widthR = image.getWidth(); int
     * heightR = image.getHeight(); Integer difW = maxWidht - widthR; Integer
     * difH = maxHeight - heightR; Integer widthFinal; Integer heightFinal; /*
     * if (difW < 0) { difW = difW * -1; } if (difH < 0) { difH = difH * -1; }
     */
    /*
     * if (difW < difH) { if ((maxWidht - widthR) >= 0) { widthFinal = widthR; }
     * else { widthFinal = maxWidht; } heightFinal = ((heightR * widthFinal) /
     * widthR); } else { if ((maxHeight - heightR) >= 0) { heightFinal =
     * heightR; } else { heightFinal = maxHeight; } widthFinal = ((widthR *
     * heightFinal) / heightR); } component.setHeight(heightMini+"px");
     * component.setWidth(widthMini+"px"); } catch (Exception ex) {
     * ex.printStackTrace(); }
    }
     */

    public List<EntityImage> getImagesCarouselables() {
        return imagesCarouselables;
    }

    public EntityImage getImagesCarouselablesInit() {
        return imagesCarouselablesInit;
    }

    /*
     * @Override protected void beforeQuery() { List<Proyecto> proy ectos =
     * crudService.findAll(Proyecto.class); if
     * (getParameter("plantillaProyecto") == null) {
     * getParams().add("plantillaProyecto", new Inlist<Proyecto>(proyectos)); }
     * }
     */
    public void eliminarFoto() {
    }
    private Popup popup;

    public Div getDiv() {
        popup = new Popup();

        popup.setWidth("180px");
        {
            Groupbox groupbox = new Groupbox();
            groupbox.setParent(popup);
            groupbox.setSclass("z-demo-config");
            groupbox.setClosable(false);
            groupbox.setTitle("Opciones");
            {
                Toolbar toolbar = new Toolbar();
                toolbar.setParent(groupbox);
                {
                    Toolbarbutton toolbarbutton = new Toolbarbutton("Ampliar");
                    toolbarbutton.setParent(toolbar);
                    toolbarbutton.addEventListener(Events.ON_CLICK, new EventListener() {

                        @Override
                        public void onEvent(Event t) throws Exception {
                        }
                    });
                    //***********
                    Toolbarbutton b_editar = new Toolbarbutton("Editar");
                    b_editar.setParent(toolbar);
                    b_editar.addEventListener(Events.ON_CLICK, new EventListener() {

                        @Override
                        public void onEvent(Event t) throws Exception {
                            imageListener.onEdit(imagesCarouselablesInit);
                            loadAll();
                        }
                    });
                    //***********
                    Toolbarbutton b_eliminar = new Toolbarbutton("Eliminar");
                    b_eliminar.setParent(toolbar);
                    b_eliminar.addEventListener(Events.ON_CLICK, new EventListener() {

                        @Override
                        public void onEvent(Event t) throws Exception {
                            imageListener.onDelete(imagesCarouselablesInit);
                            loadAll();
                        }
                    });
                    //***********
                }
            }
        }
        //*************
        Div div = new Div();
        div.setWidth("100%");
        div.setHeight("600px");
        {
            Borderlayout borderlayout = new Borderlayout();
            borderlayout.setParent(div);
            {
                North north = new North();
                north.setParent(borderlayout);
                {
                    Div div1 = new Div();
                    div1.setParent(north);
                    div1.setHeight("400px");
                    div1.setWidth("100%");
                    div1.setAlign("center");
                    div1.setStyle("border: solid 1px gray; padding: 5px");
                    {
                        imagenPrincipal = new Image();
                        imagenPrincipal.setParent(div1);
                        imagenPrincipal.addEventListener(Events.ON_CLICK, new EventListener() {

                            @Override
                            public void onEvent(Event t) throws Exception {
                                popup.setPage(ZKUtil.getFirstPage());
                                popup.open(imagenPrincipal, "overlap");
                            }
                        });
                    }
                }
                Center center = new Center();
                center.setParent(borderlayout);
                {
                    Div div2 = new Div();
                    div2.setParent(center);
                    {
                        rullet = new Hlayout();
                        rullet.setParent(div2);
                        rullet.setStyle("overflow:auto");
                        rullet.setWidth("100%");
                        rullet.setHeight("90px");
                        Div div3 = new Div();
                        div3.setParent(div2);
                        div3.setWidth("100%");
                        {
                            observ = new Label();
                            observ.setParent(div3);
                            observ.setWidth("100%");
                        }
                    }
                }
            }
        }
        loadAll();
        return div;
    }

    public void setListener(ImageListener imageListener) {
        this.imageListener = imageListener;
    }
}